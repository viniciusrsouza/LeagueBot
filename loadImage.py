#!/usr/bin/python
import json
import urllib.request as req
from PIL import Image


def main():
    champion_json = open("temp/champion.json", "r")
    champion = json.loads(champion_json.read())
    download_images(champion)
    create_final_image()


def download_images(champion):
    imgurl = champion["icon"]
    req.urlretrieve("http://" + imgurl, "temp/icon.png")

    i = 0
    for rune in champion["runes"]:
        if i == 0:
            name = "temp/main_rune.png"
        else:
            name = "temp/rune" + str(i) + ".png"

        i += 1
        req.urlretrieve("http://" + rune, name)

    i = 1
    for attr in champion["attributes"]:
        name = "temp/attribute" + str(i) + ".png"
        i += 1
        req.urlretrieve("http://" + attr, name)

    req.urlretrieve("http://" + champion["spells"]["first"], "temp/first_spell.png")
    req.urlretrieve("http://" + champion["spells"]["second"], "temp/second_spell.png")


def create_final_image():
    final_image = Image.open("template.png").convert("RGBA")

    spells = (Image.open("temp/first_spell.png").resize((64, 64)),
              Image.open("temp/second_spell.png").resize((64, 64)))
    final_image.paste(spells[0], box=(9, 215))
    final_image.paste(spells[1], box=(92, 215))
    spells[0].close()
    spells[1].close()

    attributes = (
        Image.open("temp/attribute1.png").resize((48, 48)),
        Image.open("temp/attribute2.png").resize((48, 48)),
        Image.open("temp/attribute3.png").resize((48, 48))
    )

    final_image = trans_paste(attributes[0], final_image, (340, 231))
    final_image = trans_paste(attributes[1], final_image, (340, 290))
    final_image = trans_paste(attributes[2], final_image, (340, 349))

    for img in attributes:
        img.close()

    mainrune = Image.open("temp/main_rune.png").resize((128, 128))
    final_image = trans_paste(mainrune, final_image, (204, 60))
    mainrune.close()

    runes = (
        Image.open("temp/rune1.png").resize((64, 64)),
        Image.open("temp/rune2.png").resize((64, 64)),
        Image.open("temp/rune3.png").resize((64, 64)),
        Image.open("temp/rune4.png").resize((64, 64)),
        Image.open("temp/rune5.png").resize((64, 64))
    )
    final_image = trans_paste(runes[0], final_image, (175, 209))
    final_image = trans_paste(runes[1], final_image, (175, 283))
    final_image = trans_paste(runes[2], final_image, (175, 357))
    final_image = trans_paste(runes[3], final_image, (257, 246))
    final_image = trans_paste(runes[4], final_image, (257, 320))

    for img in runes:
        img.close()

    icon = Image.open("temp/icon.png").resize((120, 120))
    final_image = trans_paste(icon, final_image, (22, 63))
    icon.close()

    final_image.save("temp/champion.png")


def trans_paste(fg_img, bg_img, box=(0, 0)):
    fg_img_trans = Image.new("RGBA", bg_img.size)
    fg_img_trans.paste(fg_img, box=box)
    bg_img = Image.alpha_composite(bg_img, fg_img_trans)
    return bg_img


if __name__ == "__main__":
    main()
