#!/usr/bin/env python3.6

import turtle
import math

angle = 8
distance = 1
i = 0

shell = turtle.Turtle()

shell.speed("fastest")
turtle.bgcolor("black")
shell.color("cyan")
shell.pensize(9  )

colors = "red", "orange", "yellow", "green", "blue", "purple"
length = 0

while i < 500:
    shell.fd(distance)
    shell.right(angle)
    distance += 0.1
    i += 1
    if length == 6:
        length = 0
    shell.color(colors[length])
    length += 1











turtle.done()
