#!/usr/bin/python
import json
import urllib.request as req


def main():
    small = 64      #spells and runes
    medium = 120    #icon
    large = 256     #main rune

    champion_json = open("champion.json", "r")
    champion = json.loads(champion_json.read())
    download_images(champion)


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


if __name__ == "__main__":
    main()
