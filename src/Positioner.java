/*    */ public class Positioner
/*    */ {
/*    */   protected int startX;
/*    */   protected int startY;
/*    */   protected int currentX;
/*    */   protected int currentY;
/*    */   protected int destinationX;
/*    */   protected int destinationY;
/*    */ 
/*    */   Positioner(int paramInt1, int paramInt2)
/*    */   {
/* 22 */     this.startX = (this.currentX = this.destinationX = paramInt1);
/* 23 */     this.startY = (this.currentY = this.destinationY = paramInt2);
/*    */   }
/*    */ 
/*    */   public void goTo(int paramInt1, int paramInt2) {
/* 27 */     this.startX = this.currentX;
/* 28 */     this.startY = this.currentY;
/* 29 */     this.destinationX = paramInt1;
/* 30 */     this.destinationY = paramInt2;
/*    */   }
/*    */ 
/*    */   public boolean isOnPlace() {
/* 34 */     if ((this.destinationX == this.currentX) && (this.destinationY == this.currentY)) {
/* 35 */       return true;
/*    */     }
/* 37 */     return false;
/*    */   }
/*    */ 
/*    */   public void oneStep() {
/* 41 */     if (isOnPlace()) {
/* 42 */       return;
/*    */     }
/* 44 */     int i = this.destinationX - this.currentX;
/* 45 */     int j = this.destinationY - this.currentY;
/* 46 */     double d = Math.sqrt(i * i + j * j);
/* 47 */     d = 1.0D / d + 0.2D;
/* 48 */     this.currentX += (int)Math.round(i * d);
/* 49 */     this.currentY += (int)Math.round(j * d);
/*    */   }
/*    */ 
/*    */   public int getX() {
/* 53 */     return this.currentX;
/*    */   }
/*    */ 
/*    */   public int getY() {
/* 57 */     return this.currentY;
/*    */   }
/*    */ }
