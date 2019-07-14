#!/usr/bin/python
import json
import urllib.request as req
from PIL import Image


def main():
    champion_json = open("champion.json", "r")
    champion = json.loads(champion_json.read())
    download_images(champion)
    create_final_image()


def download_images(champion):
    imgurl = champion["icon"]
    req.urlretrieve("http://" + imgurl, "icon.png")

    i = 0
    for rune in champion["runes"]:
        if i == 0:
            name = "main_rune.png"
        else:
            name = "rune" + str(i) + ".png"

        i += 1
        req.urlretrieve("http://" + rune, name)

    req.urlretrieve("http://" + champion["spells"]["first"], "first_spell.png")
    req.urlretrieve("http://" + champion["spells"]["second"], "second_spell.png")


def create_final_image():
    final_image = Image.open("template.png").convert("RGBA")

    spells = (Image.open("first_spell.png").resize((64, 64)),
              Image.open("second_spell.png").resize((64, 64)))
    final_image.paste(spells[0], box=(9, 215))
    final_image.paste(spells[1], box=(92, 215))
    spells[0].close()
    spells[1].close()

    mainrune = Image.open("main_rune.png").resize((128, 128))
    final_image = trans_paste(mainrune, final_image, (204, 60))
    mainrune.close()

    runes = (
        Image.open("rune1.png").resize((64, 64)),
        Image.open("rune2.png").resize((64, 64)),
        Image.open("rune3.png").resize((64, 64)),
        Image.open("rune4.png").resize((64, 64)),
        Image.open("rune5.png").resize((64, 64))
    )
    final_image = trans_paste(runes[0], final_image, (195, 209))
    final_image = trans_paste(runes[1], final_image, (195, 282))
    final_image = trans_paste(runes[2], final_image, (195, 355))
    final_image = trans_paste(runes[3], final_image, (277, 239))
    final_image = trans_paste(runes[4], final_image, (277, 317))

    for img in runes:
        img.close()

    icon = Image.open("icon.png").resize((120, 120))
    final_image = trans_paste(icon, final_image, (22, 63))
    icon.close()

    final_image.save("champion.png")


def trans_paste(fg_img, bg_img, box=(0, 0)):
    fg_img_trans = Image.new("RGBA", bg_img.size)
    fg_img_trans.paste(fg_img, box=box)
    bg_img = Image.alpha_composite(bg_img, fg_img_trans)
    return bg_img


if __name__ == "__main__":
    main()
