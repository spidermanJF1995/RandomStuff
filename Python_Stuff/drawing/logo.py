#!/usr/bin/env python3.6

import turtle
import math

angle = 50 
angle2 = 20
distance = 340
holder = 1
x = (0)
y = (0)

shell = turtle.Turtle()

shell.speed("fastest")
turtle.bgcolor("black")
shell.color("cyan")
shell.up()
shell.setpos(x, y)
shell.down()

colors = "Blue", "Cyan", "Orange", "Red"
length = 0

while holder < distance:
    if length == len(colors):
        length = 0
    shell.color(colors[length])
    length += 1
    shell.fd(holder)
    shell.left(angle2)
    shell.fd(holder)
    shell.left(angle2)
    shell.fd(holder)
    shell.left(angle)
    holder += 1










turtle.done()


