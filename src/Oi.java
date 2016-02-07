/*    */ public class Oi
/*    */   implements BTreeComparable
/*    */ {
/*    */   protected final int x;
/*    */ 
/*    */   Oi(int paramInt)
/*    */   {
/* 22 */     this.x = paramInt;
/*    */   }
/*    */ 
/*    */   public int getValue() {
/* 26 */     return this.x;
/*    */   }
/*    */ 
/*    */   public int compareTo(BTreeComparable paramBTreeComparable) {
/* 30 */     Oi localOi = (Oi)paramBTreeComparable;
/* 31 */     int i = localOi.getValue();
/*    */ 
/* 33 */     if (this.x == i)
/* 34 */       return 0;
/* 35 */     if (this.x < i) {
/* 36 */       return -1;
/*    */     }
/* 38 */     return 1;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 42 */     return String.valueOf(this.x);
/*    */   }
/*    */ }