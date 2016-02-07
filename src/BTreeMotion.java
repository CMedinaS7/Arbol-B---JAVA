/*     */ import java.awt.Graphics;
/*     */ 
/*     */ public class BTreeMotion extends BTree
/*     */ {
/*     */   private Positioner rootGraphicsStart;
/*     */   private Positioner positionA;
/*     */   private Positioner positionB;
/*     */   private BTreeComparable numberA;
/*     */   private BTreeComparable numberB;
/*     */   private BTreeNode nodeA;
/*     */   private BTreeNode storedLeft;
/*     */   private BTreeNode storedRight;
/*     */   private int state;
/*     */   private int swapIndex;
/*     */ 
/*     */   BTreeMotion(int paramInt, Positioner paramPositioner)
/*     */   {
/*  36 */     super(paramInt);
/*  37 */     this.rootGraphicsStart = paramPositioner;
/*  38 */     this.root = new BTreeNode(paramInt, true, new Positioner(paramPositioner.getX(), paramPositioner.getY()));
/*  39 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   public void findAnimated(BTreeComparable paramBTreeComparable)
/*     */   {
/*  44 */     if (this.root == null) {
/*  45 */       throw new IllegalStateException();
/*     */     }
/*  47 */     this.positionA = new Positioner(this.rootGraphicsStart.getX(), -20);
/*  48 */     this.positionA.goTo(this.rootGraphicsStart.getX(), this.rootGraphicsStart.getY());
/*     */ 
/*  50 */     this.numberA = paramBTreeComparable;
/*  51 */     this.nodeA = this.root;
/*  52 */     this.state = 10;
/*     */   }
/*     */ 
/*     */   private void findAnimatedN() {
/*  56 */     if (this.nodeA.isEmpty()) {
/*  57 */       this.state = 13;
/*  58 */       return;
/*     */     }
/*  60 */     int i = this.nodeA.findKeyPosition(this.numberA);
/*  61 */     int j = this.nodeA.position.getX() + (((-this.order << 1) + i) * 20 >> 1);
/*  62 */     this.positionA.goTo(j, this.nodeA.position.getY());
/*  63 */     if ((i & 0x1) == 1) {
/*  64 */       this.state = 13;
/*     */     } else {
/*  66 */       this.nodeA = this.nodeA.getChild(i >> 1);
/*  67 */       this.state = 11;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void findAnimatedL() {
/*  72 */     this.positionA.goTo(this.positionA.getX(), this.positionA.getY() + 20);
/*  73 */     this.state = 12;
/*     */   }
/*     */ 
/*     */   private void findAnimatedD() {
/*  77 */     if (this.nodeA == null) {
/*  78 */       this.state = 13;
/*  79 */       return;
/*     */     }
/*  81 */     this.positionA.goTo(this.nodeA.position.getX(), this.nodeA.position.getY());
/*  82 */     this.state = 10;
/*     */   }
/*     */ 
/*     */   public void addAnimated(BTreeComparable paramBTreeComparable)
/*     */   {
/*  87 */     if (find(paramBTreeComparable) != null) {
/*  88 */       findAnimated(paramBTreeComparable);
/*  89 */       return;
/*     */     }
/*     */ 
/*  92 */     findLeafAnimated(paramBTreeComparable);
/*     */   }
/*     */ 
/*     */   private void findLeafAnimated(BTreeComparable paramBTreeComparable)
/*     */   {
/*  98 */     findAnimated(paramBTreeComparable);
/*  99 */     this.state = 20;
/*     */   }
/*     */ 
/*     */   private void findLeafAnimatedN() {
/* 103 */     if ((this.nodeA.isEmpty()) || (this.nodeA.isLeaf())) {
/* 104 */       this.state = 23;
/* 105 */       return;
/*     */     }
/* 107 */     findAnimatedN();
/* 108 */     this.state += 10;
/*     */   }
/*     */ 
/*     */   private void findLeafAnimatedL() {
/* 112 */     findAnimatedL();
/* 113 */     this.state += 10;
/*     */   }
/*     */ 
/*     */   private void findLeafAnimatedD() {
/* 117 */     findAnimatedD();
/* 118 */     this.state += 10;
/*     */   }
/*     */ 
/*     */   private void addHereAnimated()
/*     */   {
/* 123 */     if (this.nodeA.isEmpty()) {
/* 124 */       addHereToEmpty(this.nodeA, this.numberA, this.storedLeft, this.storedRight);
/* 125 */       this.state = 39;
/* 126 */       return;
/*     */     }
/*     */ 
/* 129 */     int i = this.nodeA.findKeyPosition(this.numberA);
/* 130 */     if ((i & 0x1) == 1) {
/* 131 */       throw new IllegalStateException("filled: " + this.nodeA.getFilled() + " position: " + i + " item: " + this.numberA.toString());
/*     */     }
/* 133 */     i >>= 1;
/* 134 */     if (this.nodeA.getChild(i) != this.storedLeft) {
/* 135 */       throw new IllegalStateException();
/*     */     }
/* 137 */     if (this.nodeA.getFilled() < 2 * this.nodeA.getOrder()) {
/* 138 */       addHereNotFull(this.nodeA, this.numberA, this.storedLeft, this.storedRight, i);
/* 139 */       this.state = 39;
/* 140 */       return;
/*     */     }
/*     */ 
/* 143 */     int j = this.nodeA.position.getX() + this.order * 20;
/* 144 */     BTreeComparable[] arrayOfBTreeComparable1 = new BTreeComparable[this.order * 2 + 1];
/* 145 */     BTreeNode[] arrayOfBTreeNode1 = new BTreeNode[this.order * 2 + 2];
/* 146 */     addHereMakeTmp(this.nodeA, this.numberA, this.storedLeft, this.storedRight, arrayOfBTreeComparable1, arrayOfBTreeNode1, i);
/* 147 */     BTreeNode localBTreeNode = addHereNewRight(this.nodeA, arrayOfBTreeComparable1, arrayOfBTreeNode1, this.order);
/* 148 */     localBTreeNode.position = new Positioner(j + 20, this.nodeA.position.getY());
/* 149 */     addHereSetLeft(this.nodeA, arrayOfBTreeComparable1, arrayOfBTreeNode1);
/*     */ 
/* 151 */     if (!localBTreeNode.isLeaf()) {
/* 152 */       for (int k = 0; k <= this.order; k++)
/* 153 */         localBTreeNode.getChild(k).parent = localBTreeNode;
/*     */     }
/* 155 */     if (this.nodeA == this.root) {
/* 156 */       BTreeComparable[] arrayOfBTreeComparable2 = { arrayOfBTreeComparable1[this.order] };
/* 157 */       BTreeNode[] arrayOfBTreeNode2 = { this.nodeA, localBTreeNode };
/* 158 */       this.root = new BTreeNode(this.order, null, arrayOfBTreeComparable2, arrayOfBTreeNode2, false, new Positioner(j, this.nodeA.position.getY()));
/* 159 */       this.nodeA.parent = this.root;
/* 160 */       localBTreeNode.parent = this.root;
/* 161 */       this.state = 39;
/*     */     } else {
/* 163 */       this.storedLeft = this.nodeA;
/*     */ 
/* 165 */       this.storedRight = localBTreeNode;
/* 166 */       this.numberA = arrayOfBTreeComparable1[this.order];
/* 167 */       this.positionA = new Positioner(this.nodeA.position.getX(), this.nodeA.position.getY());
/* 168 */       this.nodeA = this.nodeA.getParent();
/* 169 */       this.positionA.goTo(this.nodeA.position.getX() + (this.nodeA.findKeyPosition(this.numberA) - 2 * this.order) * 20 / 2, this.nodeA.position.getY() + 20);
/* 170 */       this.nodeA.hidden = localBTreeNode;
/* 171 */       this.nodeA.hiddenPosition = this.storedLeft;
/* 172 */       this.state = 31;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void deleteAnimated(BTreeComparable paramBTreeComparable)
/*     */   {
/* 178 */     if (find(paramBTreeComparable) == null) {
/* 179 */       findAnimated(paramBTreeComparable);
/* 180 */       return;
/*     */     }
/* 182 */     findDeleteAnimated(paramBTreeComparable);
/*     */   }
/*     */ 
/*     */   private void findDeleteAnimated(BTreeComparable paramBTreeComparable)
/*     */   {
/* 187 */     findAnimated(paramBTreeComparable);
/* 188 */     this.state = 40;
/*     */   }
/*     */ 
/*     */   private void findDeleteAnimatedN() {
/* 192 */     if ((this.nodeA.isEmpty()) || (this.nodeA.isLeaf())) {
/* 193 */       this.state = 43;
/* 194 */       return;
/*     */     }
/* 196 */     findAnimatedN();
/* 197 */     this.state += 30;
/*     */   }
/*     */ 
/*     */   private void findDeleteAnimatedL() {
/* 201 */     findAnimatedL();
/* 202 */     this.state += 30;
/*     */   }
/*     */ 
/*     */   private void findDeleteAnimatedD() {
/* 206 */     findAnimatedD();
/* 207 */     this.state += 30;
/*     */   }
/*     */ 
/*     */   public void deleteTestSwap()
/*     */   {
/* 212 */     BTreeNode localBTreeNode = this.nodeA;
/* 213 */     int i = localBTreeNode.findKeyPosition(this.numberA) >> 1;
/* 214 */     if (localBTreeNode.isLeaf() == false) {
/* 215 */       this.positionB = new Positioner(localBTreeNode.position.getX() - (this.order - 1) * 2 * 20 / 2, localBTreeNode.position.getY());
/* 216 */       this.numberB = localBTreeNode.key[0];
/* 217 */       this.positionA.goTo(this.positionB.getX(), this.positionB.getY());
/* 218 */       this.positionB.goTo(this.positionA.getX(), this.positionA.getY());
/* 219 */       this.nodeA = localBTreeNode;
/* 220 */       this.swapIndex = i;
/* 221 */       this.state = 51;
/*     */     } else {
/* 223 */       this.state = 52;
/*     */     }
/*     */   }
/*     */ 
/* 227 */   public void deleteSwap() { this.nodeA = swapWithLeaf(this.nodeA, this.swapIndex);
/* 228 */     this.state = 52; }
/*     */ 
/*     */   public void deleteRemove()
/*     */   {
/* 232 */     removeOne(this.nodeA, this.nodeA.findKeyPosition(this.numberA));
/*     */ 
/* 234 */     if (this.nodeA == this.root) {
/* 235 */       this.state = 0;
/* 236 */       return;
/*     */     }
/*     */ 
/* 239 */     this.state = 53;
/*     */   }
/*     */ 
/*     */   public void deleteNotEnough() {
/* 243 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   private void reposition()
/*     */   {
/* 250 */     this.root.goTo(this.rootGraphicsStart.getX(), this.rootGraphicsStart.getY());
/*     */   }
/*     */ 
/*     */   public void draw(Graphics paramGraphics) {
/* 254 */     if (this.state == 10) {
/* 255 */       if (this.positionA.isOnPlace())
/* 256 */         findAnimatedN();
/* 257 */     } else if (this.state == 11) {
/* 258 */       if (this.positionA.isOnPlace())
/* 259 */         findAnimatedL();
/* 260 */     } else if (this.state == 12) {
/* 261 */       if (this.positionA.isOnPlace())
/* 262 */         findAnimatedD();
/* 263 */     } else if (this.state == 13) {
/* 264 */       if (this.positionA.isOnPlace()) {
/* 265 */         this.positionA = null;
/* 266 */         this.nodeA = null;
/* 267 */         this.numberA = null;
/* 268 */         this.state = 0;
/*     */       }
/* 270 */     } else if (this.state == 20) {
/* 271 */       if (this.positionA.isOnPlace())
/* 272 */         findLeafAnimatedN();
/* 273 */     } else if (this.state == 21) {
/* 274 */       if (this.positionA.isOnPlace())
/* 275 */         findLeafAnimatedL();
/* 276 */     } else if (this.state == 22) {
/* 277 */       if (this.positionA.isOnPlace())
/* 278 */         findLeafAnimatedD();
/* 279 */     } else if (this.state == 23) {
/* 280 */       if (this.positionA.isOnPlace()) {
/* 281 */         this.positionA = null;
/* 282 */         this.storedLeft = (this.storedRight = null);
/* 283 */         this.state = 30;
/*     */       }
/* 285 */     } else if (this.state == 30) {
/* 286 */       addHereAnimated();
/* 287 */     } else if (this.state == 31) {
/* 288 */       if (this.positionA.isOnPlace()) {
/* 289 */         this.positionA = null;
/* 290 */         this.nodeA.hidden = null;
/* 291 */         this.nodeA.hiddenPosition = null;
/* 292 */         this.state = 30;
/*     */       }
/* 294 */     } else if (this.state == 39) {
/* 295 */       this.positionA = null;
/* 296 */       this.nodeA = null;
/* 297 */       this.storedLeft = (this.storedRight = null);
/* 298 */       this.state = 0;
/* 299 */     } else if (this.state == 40) {
/* 300 */       if (this.positionA.isOnPlace())
/* 301 */         findDeleteAnimatedN();
/* 302 */     } else if (this.state == 41) {
/* 303 */       if (this.positionA.isOnPlace())
/* 304 */         findDeleteAnimatedL();
/* 305 */     } else if (this.state == 42) {
/* 306 */       if (this.positionA.isOnPlace())
/* 307 */         findDeleteAnimatedD();
/* 308 */     } else if (this.state == 43) {
/* 309 */       if (this.positionA.isOnPlace())
/* 310 */         this.state = 50;
/*     */     }
/* 312 */     else if (this.state == 50) {
/* 313 */       deleteTestSwap();
/* 314 */     } else if (this.state == 51) {
/* 315 */       if ((this.positionA.isOnPlace()) && (this.positionB.isOnPlace()))
/* 316 */         deleteSwap();
/* 317 */     } else if (this.state == 52) {
/* 318 */       deleteRemove();
/* 319 */     } else if (this.state == 53)
/*     */     {
/* 327 */       deleteNotEnough();
/*     */     }
/* 329 */     reposition();
/* 330 */     this.root.draw(paramGraphics);
/* 331 */     if (this.positionA != null) {
/* 332 */       paramGraphics.drawImage(BTreeAnimation.ball, this.positionA.getX() - 10, this.positionA.getY() - 10, BTreeAnimation.getApplet());
/* 333 */       BTreeAnimation.drawText(paramGraphics, this.numberA.toString(), this.positionA.getX(), this.positionA.getY());
/*     */     }
/* 335 */     if (this.positionB != null) {
/* 336 */       paramGraphics.drawImage(BTreeAnimation.ball, this.positionB.getX() - 10, this.positionB.getY() - 10, BTreeAnimation.getApplet());
/* 337 */       BTreeAnimation.drawText(paramGraphics, this.numberB.toString(), this.positionB.getX(), this.positionB.getY());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void oneStep() {
/* 342 */     this.rootGraphicsStart.oneStep();
/* 343 */     if (this.positionA != null)
/* 344 */       this.positionA.oneStep();
/* 345 */     if (this.positionB != null)
/* 346 */       this.positionB.oneStep();
/* 347 */     this.root.oneStep();
/*     */   }
/*     */ 
/*     */   public boolean isOnPlace() {
/* 351 */     if (this.state != 0)
/* 352 */       return false;
/* 353 */     if ((this.positionA != null) && (this.positionA.isOnPlace() == false))
/* 354 */       return false;
/* 355 */     if ((this.positionB != null) && (this.positionB.isOnPlace() == false))
/* 356 */       return false;
/* 357 */     return (this.root.isOnPlace()) && (this.rootGraphicsStart.isOnPlace());
/*     */   }
/*     */ 
/*     */   public void goTo(int paramInt1, int paramInt2) {
/* 361 */     this.rootGraphicsStart.goTo(paramInt1, paramInt2);
/*     */   }
/*     */ }
