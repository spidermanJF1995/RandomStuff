#!/usr/bin/env python3.6

# The above import is needed to run this file as an executable
# NOTE: chmod +x file_name.py is needed to actually make the file an executable

# The below import is needed to use turtle
import turtle
import math

distance = 200
speed = 5
angle = 90
background_color = "black"
diagonal = (math.sqrt(distance * distance + distance * distance))

# Creates a new turtle object
shell = turtle.Turtle()

# turtle.bgcolor("Color name") sets the background color
turtle.bgcolor(background_color)

# Turtle_Name.color("Color Name") sets the pen color
shell.color("blue")

# Turtle_Name sets the draw speed
shell.speed(speed)

# Turtle_Name.forward(distance) moves the turtle object that distance in the direction the turtle is facing
shell.forward(distance)

# Turtle_Name.left(angle) rotates the turtle object the specified angle (in degrees)
shell.left(angle)

shell.color("purple")
shell.forward(distance)

shell.color("orange")
shell.left(angle)
shell.forward(distance)

shell.color("green")
shell.left(angle)
shell.forward(distance)
#end square top right
#******************************************************************************************
shell.penup() # Turtle_Name.penup() picks the pen up so that it won't draw when moving
shell.setpos(0, 0) # Turtle_Name.setpos(x,y) sets the location of the pen. [(0, 0) is the center of screen]
shell.pendown() # Turtle_Name.pendown() sets the pen back down so that it can draw again

shell.right(angle)
shell.color("red")
shell.forward(distance)

shell.right(angle)
shell.color("cyan")
shell.forward(distance)

shell.right(angle)
shell.color("purple")
shell.forward(distance)

#end square top left 
#******************************************************************************************
shell.penup() # Turtle_Name.penup() picks the pen up so that it won't draw when moving
shell.setpos(0, 0) # Turtle_Name.setpos(x,y) sets the location of the pen. [(0, 0) is the center of screen]
shell.pendown() # Turtle_Name.pendown() sets the pen back down so that it can draw again

shell.right(angle)
shell.color("red")
shell.forward(distance)

shell.right(angle)
shell.color("cyan")
shell.forward(distance)

shell.right(angle)
shell.color("purple")
shell.forward(distance)

#end square bottom left 
#******************************************************************************************
shell.penup() # Turtle_Name.penup() picks the pen up so that it won't draw when moving
shell.setpos(0,-200) # Turtle_Name.setpos(x,y) sets the location of the pen. [(0, 0) is the center of screen]
shell.pendown() # Turtle_Name.pendown() sets the pen back down so that it can draw again

shell.right(angle)
shell.color("cyan")
shell.forward(distance)

shell.left(angle)
shell.color("green")
shell.forward(distance)

#end square bottom left 
#******************************************************************************************
shell.penup()
shell.color("purple")
shell.setpos(-200, 200)
shell.right(135)
shell.pendown()
shell.forward(diagonal * 2)

shell.penup()
shell.setpos(200, 200)
shell.right(90)
shell.pendown()
shell.forward(diagonal * 2)























# turtle.done shows that the program has finisthed executing
turtle.done()
