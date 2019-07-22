import sys
import numpy as np

# -1 Index out of bounds
# -2 File not found
#


def decode(champion, champions):
    values = []
    for name in champions:
        matrix = np.zeros((len(name)+1, len(champion)+1))
        for i in range(len(name)+1):
            matrix[i][0] = i
        for j in range(len(champion)+1):
            matrix[0][j] = j

        for i in range(1, len(name)+1):
            for j in range(1, len(champion)+1):
                minimum = min(matrix[i-1][j-1], matrix[i-1][j], matrix[i][j-1])
                if name[i-1] == champion[j-1]:
                    matrix[i][j] = minimum
                else:
                    matrix[i][j] = minimum + 1

        values.append(matrix[len(name)][len(champion)])
    return values.index(min(values))


def main():
    try:
        champion = sys.argv[1]
    except IndexError:
        return -1
    try:
        champions_csv = open("champions.csv")
    except FileNotFoundError:
        return -2
    champions = champions_csv.read().replace("\n", "").split(",")

    index = int(decode(champion, champions))
    print(champions[index])
    sys.exit(index)


if __name__ == '__main__':
    main()
