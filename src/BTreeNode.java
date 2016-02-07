/*     */ import java.awt.Graphics;
/*     */ 
/*     */ class BTreeNode
/*     */ {
/*     */   int order;
/*     */   BTreeComparable[] key;
/*     */   BTreeNode[] child;
/*     */   BTreeNode parent;
/*     */   int filled;
/*     */   boolean isLeaf;
/*     */   public Positioner position;
/*     */   public BTreeNode hidden;
/*     */   public BTreeNode hiddenPosition;
/*     */ 
/*     */   BTreeNode(int paramInt, boolean paramBoolean)
/*     */   {
/*  33 */     this.order = paramInt;
/*  34 */     int i = paramInt << 1;
/*  35 */     this.key = new BTreeComparable[i];
/*  36 */     this.child = new BTreeNode[i | 0x1];
/*  37 */     this.parent = null;
/*  38 */     this.filled = 0;
/*  39 */     this.isLeaf = paramBoolean;
/*  40 */     this.position = new Positioner(0, 0);
/*     */   }
/*     */ 
/*     */   BTreeNode(int paramInt, BTreeNode paramBTreeNode, BTreeComparable[] paramArrayOfBTreeComparable, BTreeNode[] paramArrayOfBTreeNode, boolean paramBoolean) {
/*  44 */     this(paramInt, paramBoolean);
/*     */ 
/*  46 */     if ((paramArrayOfBTreeComparable.length > paramInt << 1) || (paramArrayOfBTreeComparable.length != paramArrayOfBTreeNode.length - 1)) {
/*  47 */       throw new IllegalArgumentException("order: " + paramInt + "objects.length: " + paramArrayOfBTreeComparable.length + "theirSons.length: " + paramArrayOfBTreeNode.length);
/*     */     }
/*  49 */     this.parent = paramBTreeNode;
/*  50 */     this.filled = paramArrayOfBTreeComparable.length;
/*     */ 
/*  52 */     System.arraycopy(paramArrayOfBTreeComparable, 0, this.key, 0, this.filled);
/*  53 */     System.arraycopy(paramArrayOfBTreeNode, 0, this.child, 0, this.filled + 1);
/*     */   }
/*     */ 
/*     */   BTreeNode(int paramInt, boolean paramBoolean, Positioner paramPositioner) {
/*  57 */     this(paramInt, paramBoolean);
/*  58 */     this.position = paramPositioner;
/*     */   }
/*     */ 
/*     */   BTreeNode(int paramInt, BTreeNode paramBTreeNode, BTreeComparable[] paramArrayOfBTreeComparable, BTreeNode[] paramArrayOfBTreeNode, boolean paramBoolean, Positioner paramPositioner) {
/*  62 */     this(paramInt, paramBTreeNode, paramArrayOfBTreeComparable, paramArrayOfBTreeNode, paramBoolean);
/*  63 */     this.position = paramPositioner;
/*     */   }
/*     */ 
/*     */   public int findKeyPosition(BTreeComparable paramBTreeComparable)
/*     */   {
            int j;
/*  69 */     if (isEmpty())
/*  70 */       throw new IllegalStateException();
/*     */     int i;
/*  74 */     for (j = i = 0; i < this.filled; i++) {
/*  75 */       int k = paramBTreeComparable.compareTo(this.key[i]);
/*  76 */       if (k < 0) {
/*  77 */         return j;
/*     */       }
/*  79 */       j++;
/*     */ 
/*  81 */       if (k == 0) {
/*  82 */         return j;
/*     */       }
/*  84 */       j++;
/*     */     }
/*  86 */     return j;
/*     */   }
/*     */ 
/*     */   public int findChildPosition(BTreeNode paramBTreeNode) {
/*  90 */     if (isEmpty())
/*  91 */       throw new IllegalStateException("nodo vacio");
/*  92 */     for (int i = 0; i <= this.filled; i++)
/*  93 */       if (this.child[i] == paramBTreeNode)
/*  94 */         return i;
/*  95 */     throw new IllegalStateException("no se encuentra en nodo");
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  99 */     String str = "<[";
/*     */ 
/* 101 */     for (int i = 0; i <= this.order << 1; i++) {
/* 102 */       if (this.child[i] != null)
/* 103 */         str = str + "{" + this.child[i].toString() + "}";
/* 104 */       if (i < this.filled) {
/* 105 */         str = str + " (" + this.key[i].toString() + ") ";
/*     */       }
/* 107 */       else if (i < this.order << 1) {
/* 108 */         str = str + " . ";
/*     */       }
/*     */     }
/* 111 */     return str + "]>";
/*     */   }
/*     */ 
/*     */   public void cleanNode() {
/* 115 */     int i = this.order << 1;
/* 116 */     for (int j = this.filled; j < i; j++) {
/* 117 */       this.key[j] = null;
/* 118 */       this.child[(j + 1)] = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getOrder()
/*     */   {
/* 125 */     return this.order;
/*     */   }
/*     */ 
/*     */   public int getFilled() {
/* 129 */     return this.filled;
/*     */   }
/*     */ 
/*     */   public BTreeNode getParent() {
/* 133 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public BTreeNode getChild(int paramInt) {
/* 137 */     if ((paramInt < 0) || (paramInt > this.filled) || (isEmpty())) {
/* 138 */       throw new IndexOutOfBoundsException("filled: " + this.filled + " position: " + paramInt);
/*     */     }
/* 140 */     return this.child[paramInt];
/*     */   }
/*     */ 
/*     */   public BTreeComparable getKey(int paramInt) {
/* 144 */     if ((paramInt < 0) || (paramInt >= this.filled) || (isEmpty())) {
/* 145 */       throw new IndexOutOfBoundsException("filled: " + this.filled + " position: " + paramInt);
/*     */     }
/* 147 */     return this.key[paramInt];
/*     */   }
/*     */ 
/*     */   public boolean isLeaf() {
/* 151 */     return this.isLeaf;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/* 155 */     return getFilled() == 0;
/*     */   }
/*     */ 
/*     */   public void draw(Graphics paramGraphics)
/*     */   {
/* 171 */     int i = this.order << 1; int j = this.position.getX(); int k = this.position.getY();
/*     */ 
/* 173 */     if (!isLeaf()) {
/* 174 */       for (int m = 0; m <= this.filled; m++) {
/* 175 */         this.child[m].draw(paramGraphics);
/* 176 */         if ((this.hidden != null) && (this.hiddenPosition == this.child[m]))
/* 177 */           this.hidden.draw(paramGraphics);
/*     */       }
/*     */     }
/* 180 */     paramGraphics.drawImage(BTreeAnimation.left, j - this.order * 20 - 5, k, BTreeAnimation.getApplet());
/* 181 */     paramGraphics.drawImage(BTreeAnimation.right, j + this.order * 20, k, BTreeAnimation.getApplet());
/*     */     int n;
/* 182 */     for (int m = 0; m < i; m++) {
/* 183 */       n = (m - this.order) * 20 + j;
/* 184 */       if (m < this.filled) {
/* 185 */         paramGraphics.drawImage(BTreeAnimation.full, n, k, BTreeAnimation.getApplet());
/* 186 */         BTreeAnimation.drawText(paramGraphics, getKey(m).toString(), n + 10, k + 12);
/*     */       } else {
/* 188 */         paramGraphics.drawImage(BTreeAnimation.empty, (m - this.order) * 20 + j, k, BTreeAnimation.getApplet());
/*     */       }
/*     */     }
/*     */ 
/* 192 */     paramGraphics.setColor(BTreeAnimation.linkColor);
/* 193 */     if (!isLeaf())
/* 194 */       for (n = 0; n <= this.filled; n++)
/* 195 */         paramGraphics.drawLine(j + (n - this.order) * 20, this.position.getY() + 20, 
/* 196 */           this.child[n].position.getX(), this.child[n].position.getY());
/*     */   }
/*     */ 
/*     */   public int getWidth() {
/* 200 */     return 20 * this.order * 2 + 5 + 5;
/*     */   }
/*     */ 
/*     */   public int getFullWidth() {
/* 204 */     if (isLeaf()) {
/* 205 */       return getWidth();
/*     */     }
/* 207 */     int i = 0;
/* 208 */     for (int j = 0; j <= this.filled; j++) {
/* 209 */       i += getChild(j).getFullWidth();
/* 210 */       if (j > 0)
/* 211 */         i += 10;
/*     */     }
/* 213 */     if (this.hidden != null) {
/* 214 */       i += 10 + this.hidden.getFullWidth();
/*     */     }
/* 216 */     return i;
/*     */   }
/*     */ 
/*     */   public void oneStep() {
/* 220 */     this.position.oneStep();
/* 221 */     if (!isLeaf()) {
/* 222 */       for (int i = 0; i <= this.filled; i++)
/* 223 */         this.child[i].oneStep();
/* 224 */       if (this.hidden != null)
/* 225 */         this.hidden.oneStep();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isOnPlace() {
/* 230 */     if (!isLeaf()) {
/* 231 */       for (int i = 0; i <= this.filled; i++)
/* 232 */         if (this.child[i].isOnPlace() == false)
/* 233 */           return false;
/* 234 */       if ((this.hidden != null) && (this.hidden.isOnPlace() == false))
/* 235 */         return false;
/*     */     }
/* 237 */     return this.position.isOnPlace();
/*     */   }
/*     */ 
/*     */   public void goTo(int paramInt1, int paramInt2) {
/* 241 */     this.position.goTo(paramInt1, paramInt2);
/* 242 */     if (isLeaf()) {
/* 243 */       return;
/*     */     }
/* 245 */     int i = getFullWidth();
/* 246 */     int j = paramInt1 - i / 2;
/*     */ 
/* 248 */     for (int m = 0; m <= this.filled; m++) {
/* 249 */       int k = this.child[m].getFullWidth();
/* 250 */       this.child[m].goTo(j + k / 2, paramInt2 + 45);
/* 251 */       j += k + 10;
/* 252 */       if ((this.hidden != null) && (this.hiddenPosition == this.child[m])) {
/* 253 */         k = this.hidden.getFullWidth();
/* 254 */         this.hidden.goTo(j + k / 2, paramInt2 + 45);
/* 255 */         j += k + 10;
/*     */       }
/*     */     }
/*     */   }
/*     */ }
