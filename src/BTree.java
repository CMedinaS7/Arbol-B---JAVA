/*     */ public class BTree
/*     */ {
/*     */   protected BTreeNode root;
/*     */   int order;
/*     */ 
/*     */   BTree(int paramInt)
/*     */   {
/*  28 */     if (paramInt < 1) {
/*  29 */       paramInt = 1;
/*     */     }
/*  31 */     this.order = paramInt;
/*     */ 
/*  34 */     this.root = new BTreeNode(paramInt, true);
/*     */   }
/*     */ 
/*     */   public BTreeComparable find(BTreeComparable paramBTreeComparable)
/*     */   {
/*  40 */     if (this.root == null) {
/*  41 */       throw new IllegalStateException();
/*     */     }
/*  43 */     if (this.root.isEmpty()) {
/*  44 */       return null;
/*     */     }
/*  46 */     BTreeNode localBTreeNode = findNode(paramBTreeComparable);
/*  47 */     if (localBTreeNode == null) {
/*  48 */       throw new IllegalStateException();
/*     */     }
/*  50 */     int i = localBTreeNode.findKeyPosition(paramBTreeComparable);
/*  51 */     if ((i & 0x1) != 1) {
/*  52 */       return null;
/*     */     }
/*  54 */     return localBTreeNode.getKey(i >> 1);
/*     */   }
/*     */ 
/*     */   public void add(BTreeComparable paramBTreeComparable) {
/*  58 */     if (find(paramBTreeComparable) != null) {
/*  59 */       return;
/*     */     }
/*  61 */     BTreeNode localBTreeNode = findLeaf(paramBTreeComparable);
/*     */ 
/*  63 */     addHere(localBTreeNode, paramBTreeComparable, null, null);
/*     */   }
/*     */ 
/*     */   public void insert(BTreeComparable paramBTreeComparable) {
/*  67 */     add(paramBTreeComparable);
/*     */   }
/*     */ 
/*     */   public void delete(BTreeComparable paramBTreeComparable) {
/*  71 */     if (find(paramBTreeComparable) == null) {
/*  72 */       return;
/*     */     }
/*  74 */     BTreeNode localBTreeNode = findNode(paramBTreeComparable);
/*  75 */     int i = localBTreeNode.findKeyPosition(paramBTreeComparable) >> 1;
/*  76 */     if (localBTreeNode.isLeaf() == false) {
/*  77 */       localBTreeNode = swapWithLeaf(localBTreeNode, i);
/*  78 */       i = 0;
/*     */     }
/*     */ 
/*  81 */     removeOne(localBTreeNode, i);
/*     */ 
/*  83 */     if (localBTreeNode == this.root) {
/*  84 */       return;
/*     */     }
/*  86 */     notEnoughKeys(localBTreeNode);
/*     */   }
/*     */ 
/*     */   public void remove(BTreeComparable paramBTreeComparable) {
/*  90 */     delete(paramBTreeComparable);
/*     */   }
/*     */ 
/*     */   private BTreeNode findNode(BTreeComparable paramBTreeComparable)
/*     */   {
/*  96 */     if ((this.root == null) || (this.root.isEmpty())) {
/*  97 */       throw new IllegalStateException();
/*     */     }
/*  99 */     BTreeNode localBTreeNode = this.root;
/*     */ 
/* 102 */     while (localBTreeNode != null) {
/* 103 */       int i = localBTreeNode.findKeyPosition(paramBTreeComparable);
/* 104 */       if (((i & 0x1) == 1) || (localBTreeNode.isLeaf()))
/* 105 */         return localBTreeNode;
/* 106 */       localBTreeNode = localBTreeNode.getChild(i >> 1);
/*     */     }
/* 108 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   private BTreeNode findLeaf(BTreeComparable paramBTreeComparable) {
/* 112 */     BTreeNode localBTreeNode = this.root;
/*     */ 
/* 115 */     while (localBTreeNode != null) {
/* 116 */       if (localBTreeNode.isLeaf())
/* 117 */         return localBTreeNode;
/* 118 */       int i = localBTreeNode.findKeyPosition(paramBTreeComparable);
/* 119 */       if ((i & 0x1) == 1) {
/* 120 */         i--;
/*     */       }
/* 122 */       localBTreeNode = localBTreeNode.getChild(i >> 1);
/*     */     }
/* 124 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   protected void addHere(BTreeNode paramBTreeNode1, BTreeComparable paramBTreeComparable, BTreeNode paramBTreeNode2, BTreeNode paramBTreeNode3) {
/* 128 */     if (paramBTreeNode1.isEmpty()) {
/* 129 */       addHereToEmpty(paramBTreeNode1, paramBTreeComparable, paramBTreeNode2, paramBTreeNode3);
/* 130 */       return;
/*     */     }
/*     */ 
/* 133 */     int i = paramBTreeNode1.findKeyPosition(paramBTreeComparable);
/* 134 */     if ((i & 0x1) == 1) {
/* 135 */       throw new IllegalStateException("filled: " + paramBTreeNode1.getFilled() + " position: " + i + " item: " + paramBTreeComparable.toString());
/*     */     }
/* 137 */     i >>= 1;
/* 138 */     if (paramBTreeNode1.getChild(i) != paramBTreeNode2) {
/* 139 */       throw new IllegalStateException();
/*     */     }
/* 141 */     if (paramBTreeNode1.getFilled() < 2 * paramBTreeNode1.getOrder()) {
/* 142 */       addHereNotFull(paramBTreeNode1, paramBTreeComparable, paramBTreeNode2, paramBTreeNode3, i);
/* 143 */       return;
/*     */     }
/*     */ 
/* 146 */     BTreeComparable[] arrayOfBTreeComparable1 = new BTreeComparable[this.order * 2 + 1];
/* 147 */     BTreeNode[] arrayOfBTreeNode1 = new BTreeNode[this.order * 2 + 2];
/* 148 */     addHereMakeTmp(paramBTreeNode1, paramBTreeComparable, paramBTreeNode2, paramBTreeNode3, arrayOfBTreeComparable1, arrayOfBTreeNode1, i);
/* 149 */     BTreeNode localBTreeNode = addHereNewRight(paramBTreeNode1, arrayOfBTreeComparable1, arrayOfBTreeNode1, this.order);
/* 150 */     addHereSetLeft(paramBTreeNode1, arrayOfBTreeComparable1, arrayOfBTreeNode1);
/*     */ 
/* 153 */     if (!localBTreeNode.isLeaf()) {
/* 154 */       for (int j = 0; j <= this.order; j++)
/* 155 */         localBTreeNode.getChild(j).parent = localBTreeNode;
/*     */     }
/* 157 */     if (paramBTreeNode1 == this.root) {
/* 158 */       BTreeComparable[] arrayOfBTreeComparable2 = { arrayOfBTreeComparable1[this.order] };
/* 159 */       BTreeNode[] arrayOfBTreeNode2 = { paramBTreeNode1, localBTreeNode };
/* 160 */       this.root = new BTreeNode(this.order, null, arrayOfBTreeComparable2, arrayOfBTreeNode2, false);
/* 161 */       paramBTreeNode1.parent = this.root;
/* 162 */       localBTreeNode.parent = this.root;
/*     */     } else {
/* 164 */       addHere(paramBTreeNode1.getParent(), arrayOfBTreeComparable1[this.order], paramBTreeNode1, localBTreeNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addHereToEmpty(BTreeNode paramBTreeNode1, BTreeComparable paramBTreeComparable, BTreeNode paramBTreeNode2, BTreeNode paramBTreeNode3) {
/* 169 */     paramBTreeNode1.child[0] = paramBTreeNode2;
/* 170 */     paramBTreeNode1.child[1] = paramBTreeNode3;
/* 171 */     paramBTreeNode1.key[0] = paramBTreeComparable;
/* 172 */     paramBTreeNode1.filled = 1;
/*     */   }
/*     */ 
/*     */   protected void addHereNotFull(BTreeNode paramBTreeNode1, BTreeComparable paramBTreeComparable, BTreeNode paramBTreeNode2, BTreeNode paramBTreeNode3, int paramInt) {
/* 176 */     int i = paramBTreeNode1.getFilled() - paramInt; int j = paramInt;
/* 177 */     System.arraycopy(paramBTreeNode1.key, j, paramBTreeNode1.key, j + 1, i);
/* 178 */     System.arraycopy(paramBTreeNode1.child, j + 1, paramBTreeNode1.child, j + 2, i);
/* 179 */     paramBTreeNode1.key[paramInt] = paramBTreeComparable;
/* 180 */     paramBTreeNode1.child[(paramInt + 1)] = paramBTreeNode3;
/* 181 */     paramBTreeNode1.filled += 1;
/*     */   }
/*     */ 
/*     */   protected BTreeNode addHereNewRight(BTreeNode paramBTreeNode, BTreeComparable[] paramArrayOfBTreeComparable, BTreeNode[] paramArrayOfBTreeNode, int paramInt) {
/* 185 */     BTreeComparable[] arrayOfBTreeComparable = new BTreeComparable[paramInt];
/* 186 */     BTreeNode[] arrayOfBTreeNode = new BTreeNode[paramInt + 1];
/* 187 */     System.arraycopy(paramArrayOfBTreeComparable, paramInt + 1, arrayOfBTreeComparable, 0, paramInt);
/* 188 */     System.arraycopy(paramArrayOfBTreeNode, paramInt + 1, arrayOfBTreeNode, 0, paramInt + 1);
/* 189 */     return new BTreeNode(paramInt, paramBTreeNode.getParent(), arrayOfBTreeComparable, arrayOfBTreeNode, paramBTreeNode.isLeaf());
/*     */   }
/*     */ 
/*     */   protected void addHereMakeTmp(BTreeNode paramBTreeNode1, BTreeComparable paramBTreeComparable, BTreeNode paramBTreeNode2, BTreeNode paramBTreeNode3, BTreeComparable[] paramArrayOfBTreeComparable, BTreeNode[] paramArrayOfBTreeNode, int paramInt) {
/* 193 */     int i = paramBTreeNode1.getFilled() - paramInt;
/* 194 */     System.arraycopy(paramBTreeNode1.key, 0, paramArrayOfBTreeComparable, 0, paramInt);
/* 195 */     System.arraycopy(paramBTreeNode1.child, 0, paramArrayOfBTreeNode, 0, paramInt);
/* 196 */     System.arraycopy(paramBTreeNode1.key, paramInt, paramArrayOfBTreeComparable, paramInt + 1, i);
/* 197 */     System.arraycopy(paramBTreeNode1.child, paramInt + 1, paramArrayOfBTreeNode, paramInt + 2, i);
/* 198 */     paramArrayOfBTreeComparable[paramInt] = paramBTreeComparable;
/* 199 */     paramArrayOfBTreeNode[paramInt] = paramBTreeNode2;
/* 200 */     paramArrayOfBTreeNode[(paramInt + 1)] = paramBTreeNode3;
/*     */   }
/*     */ 
/*     */   protected void addHereSetLeft(BTreeNode paramBTreeNode, BTreeComparable[] paramArrayOfBTreeComparable, BTreeNode[] paramArrayOfBTreeNode) {
/* 204 */     paramBTreeNode.filled = paramBTreeNode.getOrder();
/* 205 */     paramBTreeNode.cleanNode();
/* 206 */     System.arraycopy(paramArrayOfBTreeComparable, 0, paramBTreeNode.key, 0, paramBTreeNode.getFilled());
/* 207 */     System.arraycopy(paramArrayOfBTreeNode, 0, paramBTreeNode.child, 0, paramBTreeNode.getFilled() + 1);
/*     */   }
/*     */ 
/*     */   protected BTreeNode findLogicalNext(BTreeNode paramBTreeNode, int paramInt) {
/* 211 */     paramBTreeNode = paramBTreeNode.getChild(paramInt + 1);
/* 212 */     while (paramBTreeNode.isLeaf() == false)
/* 213 */       paramBTreeNode = paramBTreeNode.getChild(0);
/* 214 */     return paramBTreeNode;
/*     */   }
/*     */ 
/*     */   protected BTreeNode swapWithLeaf(BTreeNode paramBTreeNode, int paramInt) {
/* 218 */     BTreeNode localBTreeNode = findLogicalNext(paramBTreeNode, paramInt);
/* 219 */     BTreeComparable localBTreeComparable = paramBTreeNode.key[paramInt];
/* 220 */     paramBTreeNode.key[paramInt] = localBTreeNode.key[0];
/* 221 */     localBTreeNode.key[0] = localBTreeComparable;
/* 222 */     return localBTreeNode;
/*     */   }
/*     */ 
/*     */   protected void removeOne(BTreeNode paramBTreeNode, int paramInt) {
/* 226 */     System.arraycopy(paramBTreeNode.key, paramInt + 1, paramBTreeNode.key, paramInt, paramBTreeNode.getFilled() - paramInt - 1);
/* 227 */     paramBTreeNode.filled -= 1;
/* 228 */     paramBTreeNode.cleanNode();
/*     */   }
/*     */ 
/*     */   protected void notEnoughKeys(BTreeNode paramBTreeNode) {
/* 232 */     if ((paramBTreeNode.getFilled() >= this.order) || (paramBTreeNode == this.root)) {
/* 233 */       return;
/*     */     }
/* 235 */     BTreeNode localBTreeNode1 = paramBTreeNode.getParent();
/* 236 */     int i = localBTreeNode1.findChildPosition(paramBTreeNode);
/*     */ 
/* 238 */     if ((i > 0) && (localBTreeNode1.getChild(i - 1).getFilled() > this.order)) {
/* 239 */       deleteBorrowLeft(paramBTreeNode, localBTreeNode1, i);
/* 240 */     } else if ((i < localBTreeNode1.getFilled()) && (localBTreeNode1.getChild(i + 1).getFilled() > this.order)) {
/* 241 */       deleteBorrowRight(paramBTreeNode, localBTreeNode1, i);
/*     */     }
/*     */     else
/*     */     {
/*     */       BTreeNode localBTreeNode2;
/*     */       BTreeNode localBTreeNode3;
/* 245 */       if (i > 0) {
/* 246 */         localBTreeNode2 = localBTreeNode1.getChild(--i);
/* 247 */         localBTreeNode3 = paramBTreeNode;
/*     */       } else {
/* 249 */         localBTreeNode2 = paramBTreeNode;
/* 250 */         localBTreeNode3 = localBTreeNode1.getChild(i + 1);
/*     */       }
/*     */ 
/* 253 */       deleteJoinNode(localBTreeNode2, localBTreeNode3, localBTreeNode1, i);
/*     */ 
/* 255 */       if ((localBTreeNode1 == this.root) && (localBTreeNode1.getFilled() == 0)) {
/* 256 */         this.root = localBTreeNode2;
/* 257 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 261 */     notEnoughKeys(localBTreeNode1);
/*     */   }
/*     */ 
/*     */   protected void deleteBorrowLeft(BTreeNode paramBTreeNode1, BTreeNode paramBTreeNode2, int paramInt) {
/* 265 */     System.arraycopy(paramBTreeNode1.key, 0, paramBTreeNode1.key, 1, paramBTreeNode1.filled);
/* 266 */     System.arraycopy(paramBTreeNode1.child, 0, paramBTreeNode1.child, 1, ++paramBTreeNode1.filled);
/* 267 */     paramBTreeNode1.key[0] = paramBTreeNode2.key[(paramInt - 1)];
/* 268 */     BTreeNode localBTreeNode = paramBTreeNode2.getChild(paramInt - 1);
/* 269 */     paramBTreeNode1.child[0] = localBTreeNode.child[localBTreeNode.filled];
/* 270 */     if (paramBTreeNode1.isLeaf() == false)
/* 271 */       paramBTreeNode1.child[0].parent = paramBTreeNode1;
/* 272 */     paramBTreeNode2.key[(paramInt - 1)] = localBTreeNode.key[(--localBTreeNode.filled)];
/* 273 */     localBTreeNode.cleanNode();
/*     */   }
/*     */ 
/*     */   protected void deleteBorrowRight(BTreeNode paramBTreeNode1, BTreeNode paramBTreeNode2, int paramInt) {
/* 277 */     BTreeNode localBTreeNode = paramBTreeNode2.getChild(paramInt + 1);
/* 278 */     paramBTreeNode1.key[(paramBTreeNode1.filled++)] = paramBTreeNode2.key[paramInt];
/* 279 */     paramBTreeNode2.key[paramInt] = localBTreeNode.key[0];
/* 280 */     paramBTreeNode1.child[paramBTreeNode1.filled] = localBTreeNode.child[0];
/* 281 */     if (paramBTreeNode1.isLeaf() == false)
/* 282 */       paramBTreeNode1.child[paramBTreeNode1.filled].parent = paramBTreeNode1;
/* 283 */     localBTreeNode.filled -= 1;
/* 284 */     System.arraycopy(localBTreeNode.key, 1, localBTreeNode.key, 0, localBTreeNode.filled);
/* 285 */     System.arraycopy(localBTreeNode.child, 1, localBTreeNode.child, 0, localBTreeNode.filled + 1);
/* 286 */     localBTreeNode.cleanNode();
/*     */   }
/*     */ 
/*     */   protected void deleteJoinNode(BTreeNode paramBTreeNode1, BTreeNode paramBTreeNode2, BTreeNode paramBTreeNode3, int paramInt) {
/* 290 */     BTreeComparable[] arrayOfBTreeComparable = new BTreeComparable[paramBTreeNode1.getFilled() + paramBTreeNode2.getFilled() + 1];
/* 291 */     BTreeNode[] arrayOfBTreeNode = new BTreeNode[paramBTreeNode1.getFilled() + paramBTreeNode2.getFilled() + 2];
/*     */ 
/* 293 */     System.arraycopy(paramBTreeNode1.key, 0, arrayOfBTreeComparable, 0, paramBTreeNode1.getFilled());
/* 294 */     System.arraycopy(paramBTreeNode2.key, 0, arrayOfBTreeComparable, paramBTreeNode1.getFilled() + 1, paramBTreeNode2.getFilled());
/* 295 */     arrayOfBTreeComparable[paramBTreeNode1.getFilled()] = paramBTreeNode3.key[paramInt];
/* 296 */     System.arraycopy(paramBTreeNode1.child, 0, arrayOfBTreeNode, 0, paramBTreeNode1.getFilled() + 1);
/* 297 */     System.arraycopy(paramBTreeNode2.child, 0, arrayOfBTreeNode, paramBTreeNode1.getFilled() + 1, paramBTreeNode2.getFilled() + 1);
/*     */ 
/* 299 */     System.arraycopy(arrayOfBTreeComparable, 0, paramBTreeNode1.key, 0, arrayOfBTreeComparable.length);
/* 300 */     System.arraycopy(arrayOfBTreeNode, 0, paramBTreeNode1.child, 0, arrayOfBTreeNode.length);
/* 301 */     paramBTreeNode1.filled = arrayOfBTreeComparable.length;
/*     */ 
/* 303 */     if (paramBTreeNode1.isLeaf() == false) {
/* 304 */       for (int i = 0; i < arrayOfBTreeNode.length; i++)
/* 305 */         paramBTreeNode1.child[i].parent = paramBTreeNode1;
/*     */     }
/* 307 */     System.arraycopy(paramBTreeNode3.key, paramInt + 1, paramBTreeNode3.key, paramInt, paramBTreeNode3.getFilled() - paramInt - 1);
/* 308 */     System.arraycopy(paramBTreeNode3.child, paramInt + 2, paramBTreeNode3.child, paramInt + 1, paramBTreeNode3.getFilled() - paramInt - 1);
/* 309 */     paramBTreeNode3.filled -= 1;
/*     */ 
/* 311 */     paramBTreeNode2.filled = 0;
/*     */ 
/* 313 */     paramBTreeNode1.cleanNode();
/* 314 */     paramBTreeNode2.cleanNode();
/* 315 */     paramBTreeNode3.cleanNode();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 319 */     return this.root.toString();
/*     */   }
/*     */ }
