# README #

## Group Information ##

**Team Members**
Prisha Kaushik
Clare Shen

**Group Number:** 7

**Period:**	7

**Game Title:** Block Blast

## Game Proposal ##

We would like to make Block Blast. In this game, a player attempts to place blocks in such a way that a row or a column
is entirely cleared, which gives them points. We woulud like to include all features of the game besides the revive. Each time, 
a player is given 3 blocks, which can always fit on the given board. The goal of the game is to get the highest score.
Points

Write a paragraph here describing the game you want to make.  Describe how it is played and which features
you want to include in your game.  Remember, any simple game can be scaled up with features and any complex
game can be scaled down.

Game Controls:

+ Mouse: drag and drop blocks
+ Click buttons using mouse

Game Elements:

+ Randomized blocks (based off the avalible options like squares and Ls)
+ 3 blocks are given at a time, and they can all always fit on the board
+ Blocks must be placed entirely in the board, and cannot overlap with any other pieces
+ Once placed, blocks cannot be moved
+ Board is a grid (8x8)
+ Columns and rows that are full are cleared
+ If the entire board is clear, you get extra points (+300)

How to Win:

+ Survive as long as you can (try to get the highest score)

## Link Examples ##
https://blockblastonline.com/ 

+ [Example Link](http://www.freewebarcade.com/game/tiny-empire/)

## Teacher Response ##

Your teacher can add comments and suggestions here

## Class Design and Brainstorm ##

World class - Board + Score -> whole board could be 2D array?
BorderPane -> center = board, bottom = options, top = score
Keep track of board -> if row or col is filled, clear it, add points
Check if more than one row or col is filled, add points based on that 
When a piece is added, check if overlapping, update 2D array, add point
Add three options on the bottom, make sure each option can fit in the board

Shape: essentially an array of all the individual pieces that it’s made of
Draw function: it draws the shape
Movement listener: add a listener to the shape.
if the pieces are not on the board, move all the pieces together when the mouse is dragged
Don’t do anything if the shape is on the board

Actor (Piece); each individual square

When the 3 shapes are at the bottom: each of them is a shape, or list of pieces
Once placed, the shape is forgotten about and we only care about the pieces
