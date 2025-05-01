package net.pawlrip.codecs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.pawlrip.mixin.ItemIconAccessor;
import net.pawlrip.mixin.TextureIconAccessor;

import java.util.List;

public class CodecUtil {

    /**
     * Like the default codec, but ensures that the first style that was created does not affect following parts,
     * that don't  have their own style.
     * <p>E.g. when displaying {@code [{"text": "colored text", color: "green"}, "non styled text"]}
     * {@code "none styled text"} is displayed in green,
     * although it is not styled and should use minecraft's default color.
     * <p>This is fixed by inserting an empty text at the beginning, which this codec does.
     */
    public static final Codec<Text> BETTER_TEXT = Codecs.TEXT.xmap(
            text -> {
                MutableText result = Text.empty().append(text.copyContentOnly().setStyle(text.getStyle()));
                for (Text part : text.getSiblings()) result.append(part);
                return result;
            },
            text -> text
    );

    private static final Codec<Identifier> IS_TEXTURE_ID = Identifier.CODEC.comapFlatMap(
            id -> id.getPath().startsWith("textures/") ?
                    DataResult.success(id) :
                    DataResult.error(() -> "Invalid texture path (must begin with \"textures/\"): " + id),
            id -> id
    );

    /**
     * A {@link Texture} that is described in a json file must either be of the form
     * <pre> {@code "<namespace>:textures/<path>/<texture>.png" }</pre>
     * or
     * <pre> {@code ["<namespace>:textures/<path>/<texture>.png", u1, u2, v1, v2] }</pre>
     * where the namespace may be omitted but the path must begin with {@code textures/}.
     * @apiNote The default u & v values are changed to {@code u1 = 0}, {@code u2 = 0}, {@code v1 = 1}, {@code v2 = 1}.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static final Codec<Texture> TEXTURE = Codecs.JSON_ELEMENT.flatXmap(
            element -> {
                if (!element.isJsonArray()) return IS_TEXTURE_ID.parse(JsonOps.INSTANCE, element)
                        .map(id -> new Texture(id, 0, 0, 1, 1));

                JsonArray arr = element.getAsJsonArray();
                DataResult<Identifier> res = IS_TEXTURE_ID.parse(JsonOps.INSTANCE, arr.get(0));
                if (res.error().isPresent()) return DataResult.error(() -> res.error().get().message());

                try {
                    float u1 = arr.get(1).getAsFloat();
                    float u2 = arr.get(2).getAsFloat();
                    float v1 = arr.get(3).getAsFloat();
                    float v2 = arr.get(4).getAsFloat();
                    return DataResult.success(new Texture(res.result().get(), u1, u2, v1, v2));
                } catch (UnsupportedOperationException e) {
                    return DataResult.error(() -> "Invalid float: " + e);
                }
            },
            texture -> {
                try {
                     DataResult<JsonElement> res = IS_TEXTURE_ID.encodeStart(JsonOps.INSTANCE, texture.image());
                     if (res.error().isPresent()) return DataResult.error(() -> res.error().get().message());

                    if (texture.u1() == 0 && texture.u2() == 0 && texture.v1() == 1 && texture.v2() == 1)
                        return DataResult.success(res.result().get());

                    JsonArray arr = new JsonArray(5);
                    arr.add(res.result().get());
                    arr.add(texture.u1());
                    arr.add(texture.u2());
                    arr.add(texture.v1());
                    arr.add(texture.v2());
                    return DataResult.success(arr);
                } catch (JsonParseException e) {
                    return DataResult.error(e::getMessage);
                }
            }
    );

    /**
     * This defaults to a {@link TextureIcon} and if this fails
     * (e.g. it doesn't conform with {@link CodecUtil#TEXTURE})
     * it will attempt to decode to an {@link ItemIcon}.
     */
    public static final Codec<Icon> ICON = Codec.either(
            TEXTURE.xmap(
                    TextureIcon::new,
                    textureIcon -> ((TextureIconAccessor) textureIcon).getTexture()
            ),
            Registries.ITEM.getCodec().xmap(
                    ItemIcon::new,
                    itemIcon -> ((ItemIconAccessor) itemIcon).getStack().getItem()
            )
    ).xmap(
            either -> either.map(
                    textureIcon -> textureIcon, itemIcon -> itemIcon
            ),
            icon -> (icon instanceof TextureIcon) ? Either.left((TextureIcon) icon) : Either.right((ItemIcon) icon)
    );

    public static <E extends Enum<E>> Codec<E> getEnumCodec(Class<E> enumClass) {
        return Codec.STRING.comapFlatMap(
                str -> {
                    try {
                        return DataResult.success(Enum.valueOf(enumClass, str.toUpperCase()));
                    } catch (Exception e) {
                        return DataResult.error(() -> "Cannot convert " + str + " to type of enum class " + enumClass);
                    }
                },
                Enum::name
        );
    }

    @SafeVarargs
    public static <E extends Enum<E>> Codec<E> getExcludingEnumCodec(Class<E> enumClass, E... excludes) {
        return CodecUtil.getEnumCodec(enumClass).comapFlatMap(
                e -> {
                    if (!List.of(excludes).contains(e)) return DataResult.success(e);
                    return DataResult.error(() -> "Enum type " + e.toString() + "of class " + enumClass +
                            " not allowed here (excluded).");
                },
                e -> e
        );
    }

    @SafeVarargs
    public static <E extends Enum<E>> Codec<E> getIncludingEnumCodec(Class<E> enumClass, E... includes) {
        return CodecUtil.getEnumCodec(enumClass).comapFlatMap(
                e -> {
                    if (List.of(includes).contains(e)) return DataResult.success(e);
                    return DataResult.error(() -> "Enum type " + e.toString() + "of class " + enumClass +
                            " not allowed here (not included).");
                },
                e -> e
        );
    }

    @SuppressWarnings("unchecked")
    public static <T extends Item> Codec<T> getItemClassSpecificCodec(Class<T> itemClass) {
        return Registries.ITEM.getCodec().comapFlatMap(
                item -> itemClass.isInstance(item) ?
                        DataResult.success((T) item) :
                        DataResult.error(() -> "Item " + item + " is not of type " + itemClass),
                item -> item
        );
    }

    @SuppressWarnings("unchecked")
    public static <T extends Block> Codec<T> getBlockClassSpecificCodec(Class<T> blockClass) {
        return Registries.BLOCK.getCodec().comapFlatMap(
                block -> blockClass.isInstance(block) ?
                        DataResult.success((T) block) :
                        DataResult.error(() -> "Block " + block + " is not of type " + blockClass),
                block -> block
        );
    }
}
