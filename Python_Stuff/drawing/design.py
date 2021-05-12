#!/usr/bin/env python3.6

import turtle
import math

colors = ["red", "orange", "yellow", "green", "blue", "purple"]
i = 0

time = 1 # This is like a counter. Starts at one to signify the program just starting [Don't alter] 
max_time = 300 # This is how long the program will run
distance = 20  # This determines how big the center gap is     
length = 6   # This is how much to add to distance each time

x = (0) # X-Coordinate for where to start drawing
y = (0) # Y-Coordinate for where to start drawing

shell = turtle.Turtle() # creates the turtle object

shell.speed("fastest") # sets the drawing speed
turtle.bgcolor("black") # sets the background color
shell.color("cyan") # sets the pen color
shell.up() # picks up the pen
shell.setpos(x, y) # moves the pen to the start location
shell.down() # sets down the pen
shell.right(90) # orients the pen to point in the correct starting direction


while time < max_time: # helps loop the drawing process
    if time < 6: # if this condition is met, the inner square is still being drawn and the inner angles need to be 90
        if(i == len(colors)): 
            i = 0

        shell.color(colors[i])
        i += 1

        shell.fd(distance) 
        shell.right(90)
        distance += length
        time += 1

        if(time == 5): # This is used to help with an orientaion of the pen issue
            shell.left(1)
    else: # if this condition is met, then it is time to begin continuously rotating the corners
        if(i == len(colors)): 
            i = 0

        shell.color(colors[i])
        i += 1        
        
        shell.fd(distance)
        distance += length
        shell.right(89)
        time += 1
   







turtle.done()
