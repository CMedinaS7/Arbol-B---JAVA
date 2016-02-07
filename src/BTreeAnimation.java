/*     */ import java.applet.Applet;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Button;
/*     */ import java.awt.Choice;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Label;
/*     */ import java.awt.Panel;
/*     */ import java.awt.TextComponent;
/*     */ import java.awt.TextField;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ 
/*     */ public class BTreeAnimation extends Applet
/*     */   implements Runnable
/*     */ {
/*     */   private int width;
/*     */   private int height;
/*     */   private int widthCenter;
/*     */   private int heightCenter;
/*     */   private Thread runner;
/*     */   private BTreeMotion bt;
/*     */   private int myTime;
/*     */   private BTreeAnimation.animationStatus as;
/*     */   private Panel menuBar;
/*  31 */   private static final Color bg = new Color(192, 204, 216);
/*  32 */   private static final Color menuBarColor = new Color(255, 204, 192);
/*     */ 
/*  34 */   public static final Color linkColor = new Color(216, 0, 192);
/*  35 */   private static final Font text = new Font("SansSerif", 0, 12);
/*     */   private Image currentImage;
/*     */   private Image paintImage;
/*     */   public static final int elementWidth = 20;
/*     */   public static final int elementHeight = 25;
/*     */   public static final int leftWidth = 5;
/*     */   public static final int rightWidth = 5;
/*     */   public static final int horizontalDistance = 10;
/*     */   public static final int verticalDistance = 45;
/*     */   public static final int pointerDistance = 20;
/*     */   public static final int ballCenter = 10;
/*     */   public static Image full;
/*     */   public static Image empty;
/*     */   public static Image right;
/*     */   public static Image left;
/*     */   public static Image ball;
/*     */   private static Applet applet;
/*     */   private Choice newRootRequest;
/*     */   private TextField keyIdentification;
/*     */   public static final int animationStatus_CALM = 0;
/*     */   public static final int animationStatus_CAN_MOVE = 1;
/*     */   public static final int animationStatus_CAN_OPERATE = 2;
/*     */   public static final int animationStatus_ERROR = 3;
/*     */ 
/*     */   public void init()
/*     */   {
/*  40 */     this.width = getSize().width;
/*  41 */     this.height = getSize().height;
/*     */ 
/*  43 */     this.widthCenter = (this.width / 2);
/*  44 */     this.heightCenter = (this.height / 2);
/*     */ 
/*  46 */     full = getImage(getCodeBase(), "sfull.gif");
/*  47 */     empty = getImage(getCodeBase(), "sempty.gif");
/*  48 */     right = getImage(getCodeBase(), "sright.gif");
/*  49 */     left = getImage(getCodeBase(), "sleft.gif");
/*  50 */     ball = getImage(getCodeBase(), "sball.gif");
/*  51 */     this.menuBar = menuBarCreate();
/*  52 */     this.menuBar.setEnabled(false);
/*  53 */     setLayout(new BorderLayout());
/*  54 */     applet = this;
/*  55 */     this.as = new BTreeAnimation.animationStatus();
/*  56 */     newRoot(1);
/*  57 */     this.currentImage = createImage(this.width, this.height);
/*  58 */     this.paintImage = createImage(this.width, this.height);
/*  59 */     this.runner = new Thread(this);
/*  60 */     this.runner.start();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics) {
/*  64 */     if (this.myTime < 50) {
/*  65 */       paramGraphics.setColor(bg);
/*  66 */       paramGraphics.fillRect(0, 0, this.width, this.height);
/*  67 */       drawText(paramGraphics, "ADMINISTRACION DE BASE DE DATOS \n ARBOL B", this.widthCenter, this.height * 2 / 5);
/*  68 */       paramGraphics.drawImage(left, this.width, this.height, this);
/*  69 */       paramGraphics.drawImage(full, this.width, this.height, this);
/*  70 */       paramGraphics.drawImage(empty, this.width, this.height, this);
/*  71 */       paramGraphics.drawImage(right, this.width, this.height, this);
/*  72 */       paramGraphics.drawImage(ball, this.width, this.height, this);
/*  73 */     } else if (!this.as.isError()) {
/*  74 */       paramGraphics.drawImage(this.currentImage, 0, 0, this);
/*     */     } else {
/*  76 */       paramGraphics.setColor(bg);
/*  77 */       paramGraphics.fillRect(0, 0, this.width, this.height);
/*  78 */       drawText(paramGraphics, "ERROR!", this.widthCenter, this.heightCenter);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics) {
/*  83 */     paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   public String getAppletInfo() {
/*  87 */     return "ADMINISTRACION DE BASE DE DATOS \n ARBOL B";
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     while (true)
/*     */     {
/*  95 */       if (this.myTime == 48) {
/*  96 */         applet.add(this.menuBar, "South");
/*  97 */         applet.validate();
/*  98 */         this.myTime += 1;
/*  99 */       } else if (this.myTime < 50) {
/* 100 */         this.myTime += 1;
/* 101 */       } else if (this.as.isMoving()) {
/* 102 */         this.bt.oneStep();
/* 103 */         Graphics localGraphics = this.paintImage.getGraphics();
/* 104 */         localGraphics.setColor(bg);
/* 105 */         localGraphics.fillRect(0, 0, this.width, this.height);
/* 106 */         this.bt.draw(localGraphics);
/*     */ 
/* 108 */         Image localImage = this.currentImage;
/* 109 */         this.currentImage = this.paintImage;
/* 110 */         this.paintImage = localImage;
/* 111 */         repaint();
/* 112 */         if (this.bt.isOnPlace())
/* 113 */           this.as.setState(0);
/*     */       }
/*     */       try
/*     */       {
/* 117 */         Thread.sleep(100L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static long rand(long paramLong) {
/* 126 */     return Math.round(Math.random() * paramLong);
/*     */   }
/*     */ 
/*     */   public static Applet getApplet()
/*     */   {
/* 154 */     return applet;
/*     */   }
/*     */ 
/*     */   public static void drawText(Graphics paramGraphics, String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 162 */     paramGraphics.setFont(text);
/* 163 */     FontMetrics localFontMetrics = paramGraphics.getFontMetrics(text);
/* 164 */     paramGraphics.setColor(Color.black);
/* 165 */     paramGraphics.drawString(paramString, paramInt1 - (localFontMetrics.stringWidth(paramString) >> 1), paramInt2 + (localFontMetrics.getAscent() >> 1) - 1);
/*     */   }
/*     */ 
/*     */   private void newRoot(int paramInt) {
/* 169 */     if (this.as.isCalm() == false)
/* 170 */       return;
/* 171 */     this.as.setState(2);
/* 172 */     this.bt = new BTreeMotion(paramInt, new Positioner(this.widthCenter, -40));
/* 173 */     this.bt.goTo(this.widthCenter, 50);
/* 174 */     this.as.setState(0);
/* 175 */     this.as.setState(1);
/*     */   }
/*     */ 
/*     */   private int getKeyIdentification()
/*     */   {
/*     */     int i;
/*     */     try
/*     */     {
/* 184 */       i = Integer.parseInt(this.keyIdentification.getText());
/* 185 */       if ((i < 0) || (i > 99)) {
/* 186 */         showStatus("Solo numeros entre 0 y 100!");
/* 187 */         return -1;
/*     */       }
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 190 */       showStatus("Error: Ingrese solo numeros!");
/* 191 */       return -1;
/*     */     }
/* 193 */     return i;
/*     */   }
/*     */ 
/*     */   private void performInsert() {
/* 197 */     if (this.as.isCalm() == false)
/* 198 */       return;
/* 199 */     this.as.setState(2);
/*     */ 
/* 201 */     int i = getKeyIdentification();
/* 202 */     if (i < 0) {
/* 203 */       this.as.setState(0);
/* 204 */       return;
/*     */     }
/*     */ 
/* 207 */     this.bt.addAnimated(new Oi(i));
/* 208 */     this.as.setState(0);
/* 209 */     this.as.setState(1);
/*     */   }
/*     */ 
/*     */   private void performDelete() {
/* 213 */     if (this.as.isCalm() == false)
/* 214 */       return;
/* 215 */     this.as.setState(2);
/*     */ 
/* 217 */     int i = getKeyIdentification();
/* 218 */     if (i < 0) {
/* 219 */       this.as.setState(0);
/* 220 */       return;
/*     */     }
/*     */ 
/* 225 */     this.bt.delete(new Oi(i));
/* 226 */     this.as.setState(0);
/* 227 */     this.as.setState(1);
/*     */   }
/*     */ 
/*     */   private void performSearch() {
/* 231 */     if (this.as.isCalm() == false)
/* 232 */       return;
/* 233 */     this.as.setState(2);
/*     */ 
/* 235 */     int i = getKeyIdentification();
/* 236 */     if (i < 0) {
/* 237 */       this.as.setState(0);
/* 238 */       return;
/*     */     }
/* 240 */     this.bt.findAnimated(new Oi(i));
/* 241 */     this.as.setState(0);
/* 242 */     this.as.setState(1);
/*     */   }
/*     */ 
/*     */   private Panel menuBarCreate() {
/* 246 */     Panel local1 = new Panel() {
/*     */       public void paint(Graphics paramAnonymousGraphics) {
/* 248 */         paramAnonymousGraphics.setColor(BTreeAnimation.menuBarColor);
/* 249 */         paramAnonymousGraphics.fillRect(0, 0, getSize().width, getSize().height);
/*     */       }
/*     */     };
/* 253 */     Label localLabel1 = new Label("Arbol B orden:: ");
/* 254 */     localLabel1.setBackground(menuBarColor);
/* 255 */     localLabel1.setForeground(Color.black);
/* 256 */     local1.add(localLabel1);
/*     */ 
/* 258 */     Choice localChoice = new Choice();
/* 259 */     localChoice.add("1");
/* 260 */     localChoice.add("2");
/* 261 */     localChoice.add("3");
/* 262 */     localChoice.add("4");
/* 263 */     localChoice.add("5");
/* 264 */     localChoice.addItemListener(new ItemListener() {
/*     */       public void itemStateChanged(ItemEvent paramAnonymousItemEvent) {
/* 266 */         int i = BTreeAnimation.this.newRootRequest.getSelectedIndex();
/* 267 */         if (i >= 0) BTreeAnimation.this
/* 268 */             .newRoot(i + 1);
/*     */       }
/*     */     });
/* 272 */     localChoice.select(0);
/* 273 */     localChoice.setBackground(menuBarColor);
/* 274 */     localChoice.setForeground(Color.black);
/* 275 */     this.newRootRequest = localChoice;
/* 276 */     local1.add(localChoice);
/*     */ 
/* 278 */     Label localLabel2 = new Label(" ::> numero ");
/* 279 */     localLabel2.setBackground(menuBarColor);
/* 280 */     localLabel2.setForeground(Color.black);
/* 281 */     local1.add(localLabel2);
/*     */ 
/* 283 */     TextField localTextField = new TextField(2);
/* 284 */     localTextField.setBackground(menuBarColor);
/* 285 */     localTextField.setForeground(Color.black);
/* 286 */     this.keyIdentification = localTextField;
/* 287 */     local1.add(localTextField);
/*     */ 
/* 289 */     Button localButton = new Button("buscar");
/* 290 */     localButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/* 292 */         BTreeAnimation.this.performSearch();
/*     */       }
/*     */     });
/* 295 */     local1.add(localButton);
/*     */ 
/* 297 */     localButton = new Button("insertar");
/* 298 */     localButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/* 300 */         BTreeAnimation.this.performInsert();
/*     */       }
/*     */     });
/* 303 */     local1.add(localButton);
/*     */ 
/* 305 */     localButton = new Button("borrar");
/* 306 */     localButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/* 308 */         BTreeAnimation.this.performDelete();
/*     */       }
/*     */     });
/* 311 */     local1.add(localButton);
/*     */ 
/* 313 */     return local1;
/*     */   }
/*     */ 
/*     */   class animationStatus
/*     */   {
/* 328 */     private boolean moving = this.operating = this.error = false;
/*     */     private boolean operating;
/*     */     private boolean error;
/*     */ 
/*     */     animationStatus()
/*     */     {
/*     */     }
/*     */ 
/*     */     synchronized boolean isCalm()
/*     */     {
/* 332 */       if (this.error)
/* 333 */         return false;
/* 334 */       return (!this.moving) && (!this.operating);
/*     */     }
/*     */ 
/*     */     synchronized boolean isMoving() {
/* 338 */       if (this.error)
/* 339 */         return false;
/* 340 */       return this.moving;
/*     */     }
/*     */ 
/*     */     synchronized boolean isOperating() {
/* 344 */       if (this.error)
/* 345 */         return false;
/* 346 */       return this.operating;
/*     */     }
/*     */ 
/*     */     synchronized boolean isError() {
/* 350 */       return this.error;
/*     */     }
/*     */ 
/*     */     synchronized void setState(int paramInt) {
/* 354 */       if (paramInt == 3) {
/* 355 */         this.error = true;
/* 356 */       } else if (paramInt == 0) {
/* 357 */         this.operating = false;
/* 358 */         this.moving = false;
/* 359 */         BTreeAnimation.this.menuBar.setEnabled(true);
/* 360 */       } else if ((paramInt == 1) && (this.operating == false)) {
/* 361 */         this.moving = true;
/* 362 */         BTreeAnimation.this.menuBar.setEnabled(false);
/* 363 */       } else if ((paramInt == 2) && (this.moving == false)) {
/* 364 */         this.operating = true;
/* 365 */         BTreeAnimation.this.menuBar.setEnabled(false);
/*     */       }
/*     */     }
/*     */   }
/*     */ }
