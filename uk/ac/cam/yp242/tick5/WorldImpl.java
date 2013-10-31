package uk.ac.cam.yp242.tick5;

import java.awt.Color;
import uk.ac.cam.acr31.life.World;
import java.io.Writer;
import java.awt.Graphics;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
//TODO: insert other appropriate "import" statements here

public abstract class WorldImpl implements World {

 private int width;
 private int height;
 private int generation;

 protected WorldImpl(int width, int height) {
  this.width = width;
  this.height = height;
  this.generation = 0;
 }
  
 protected WorldImpl(WorldImpl prev) {
  this.width = prev.width;
  this.height = prev.height;
  this.generation = prev.generation + 1;
 } 

 public int getWidth() { return this.width; }

 public int getHeight() { return this.height; }
  
 public int getGeneration() { return this.generation; }
 
 public int getPopulation() { return 0; }

 protected String getCellAsString(int col,int row) {
  return getCell(col,row) ? "#" : "_";
 }

 protected Color getCellAsColour(int col,int row) {
  return getCell(col,row) ? Color.BLUE : Color.WHITE;
 }  
 public void draw(Graphics g,int width, int height) {
  int worldWidth = getWidth();
  int worldHeight = getHeight();
  
  double colScale = (double)width/(double)worldWidth;
  double rowScale = (double)height/(double)worldHeight;
  
  for(int col=0; col<worldWidth; ++col) {
   for(int row=0; row<worldHeight; ++row) {
    int colPos = (int)(col*colScale);
    int rowPos = (int)(row*rowScale);
    int nextCol = (int)((col+1)*colScale);
    int nextRow = (int)((row+1)*rowScale);

    if (g.hitClip(colPos,rowPos,nextCol-colPos,nextRow-rowPos)) {
     g.setColor(getCellAsColour(col, row));
     g.fillRect(colPos,rowPos,nextCol-colPos,nextRow-rowPos);
    }
   } 
  }  
 }

 //TODO: Complete here in parent
 public World nextGeneration(int log2StepSize) {
  //Remember to call nextGeneration 2^log2StepSize times
    WorldImpl world = this;
      int iter = 1<<log2StepSize;
      for(int i=0; i<iter;i++){
         world = world.nextGeneration();
      }
      return world;
 }

 //TODO: Complete here in parent
 public void print(Writer w) {
  //Use getCellAsString to get text representation of the cell
    PrintWriter pw = new PrintWriter(w);
      pw.print("-");
      pw.println();
      for (int row = 0; row < height; row ++) {
         for (int col = 0; col < width; col++) {
            pw.print(getCellAsString(col,row)); 
         }
         pw.println();
         pw.flush();
      }
 }
 
 //TODO: Complete here in parent
 protected int countNeighbours(int col, int row) {
  //Compute the number of live neighbours
  int neighbours = 0;
      for (int irow = -1; irow < 2; irow++) {
         for (int icol = -1; icol < 2; icol++) {
            if (irow != 0 || icol != 0) {
              neighbours += getCell((col + icol),(row + irow)) ? 1 : 0;
            }
          }
      }
      return neighbours;
 }

 //TODO: Complete here in parent
 protected boolean computeCell(int col, int row) {
  //Compute whether this cell is alive or dead in the next generation
  //using "countNeighbours"
  int neighbours = countNeighbours(col, row);
      boolean current_state = getCell(col,row);
      boolean next_state = false;
      
      if (current_state)
         if (neighbours < 2 || neighbours > 3)
            next_state = false;
         else 
            next_state = true;
      else
         if (neighbours == 3)
            next_state = true;
            
      return next_state;
 }

 // Will be implemented by child class. Return true if cell (col,row) is alive.
 public abstract boolean getCell(int col,int row);

 // Will be implemented by child class. Set a cell to be live or dead.
 public abstract void setCell(int col, int row, boolean alive);

 // Will be implemented by child class. Step forward one generation.
 protected abstract WorldImpl nextGeneration();
}
