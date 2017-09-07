/*
 * Decompiled with CFR 0_122.
 */
package game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public final class Juego
extends Canvas {
    private ArrayList<Sprite> spritesStaticos = new ArrayList();
    private ArrayList<Sprite> spritesDinamicos = new ArrayList();
    private ArrayList<Enemy> enemys = new ArrayList();
    private ArrayList<Sprite> proyectiles = new ArrayList();
    private ArrayList<Sprite> proyectilesEnemigos = new ArrayList();
    private Frame ventana;
    private Sprite planeta = new Sprite();
    private Sprite fondo = new Sprite();
    private Enemy nave = new Enemy();
    private Marca marca;
    BufferedImage pantalla;
    int refreshTime = 25;
    long tiempo = System.currentTimeMillis();
    JPanel controles;

    public Juego() {
        this.nave.shield_resistance = 15.0;
        this.nave.shield_regeneration = 0.2;
        Enemy spitter = new Enemy();
        spitter.setSprite("/Imagenes/Nave/splitter.png");
        spitter.setX(500.0);
        spitter.setY(500.0);
        Enemy spitter2 = new Enemy();
        spitter2.setSprite("/Imagenes/Nave/splitter.png");
        spitter2.setX(700.0);
        spitter2.setY(500.0);
        Enemy spitter3 = new Enemy();
        spitter3.setSprite("/Imagenes/Nave/splitter.png");
        spitter3.setX(700.0);
        spitter3.setY(500.0);
        this.enemys.add(spitter);
        this.enemys.add(spitter2);
        this.enemys.add(spitter3);
        this.marca = new Marca();
        this.ventana = new JFrame();
        this.ventana.setLayout(new BorderLayout());
        this.controles = new JPanel();
        this.controles.add(new JButton("OK"));
        this.ventana.add((Component)this.controles, "South");
        this.ventana.setSize(1024, 500);
        this.ventana.setExtendedState(6);
        this.ventana.setIconImage(new ImageIcon(this.getClass().getResource("/Imagenes/Marca/marca2b.png")).getImage());
        this.ventana.add(this);
        this.ventana.setVisible(true);
        this.ventana.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.planeta.setSprite("/Imagenes/planeta-agua.png");
        this.fondo.setSprite("/Imagenes/space.jpg");
        this.planeta.setX(80.0);
        this.planeta.setY(80.0);
        this.spritesStaticos.add(this.fondo);
        this.spritesStaticos.add(this.planeta);
        this.spritesDinamicos.add(this.marca);
        this.spritesDinamicos.add(this.nave);
        MouseEvent mouse = new MouseEvent();
        this.addMouseListener(mouse);
        do {
            int i;
            if (System.currentTimeMillis() - this.tiempo <= (long)this.refreshTime) {
                continue;
            }
            if (this.marca.isVisible() && this.marca.intecerpta(this.nave)) {
                this.marca.setVisible(false);
            }
            int maxProyectiles = this.proyectiles.size();
            int maxEnemies = this.enemys.size();
            for (i = 0; i < maxProyectiles; ++i) {
                for (int j = 0; j < maxEnemies; ++j) {
                    if (!this.enemys.get(j).intecerpta(this.proyectiles.get(i))) continue;
                    this.enemys.get(j).receiveDamage(((Laser)this.proyectiles.get(i)).hit());
                }
            }
            if (maxEnemies == 0) {
                JOptionPane.showMessageDialog(this.ventana, "You Win", "Alerta", 1);
                System.exit(0);
            }
            maxProyectiles = this.proyectilesEnemigos.size();
            for (i = 0; i < maxProyectiles; ++i) {
                if (!this.nave.intecerpta(this.proyectilesEnemigos.get(i))) continue;
                this.nave.receiveDamage(((Laser)this.proyectilesEnemigos.get(i)).hit());
            }
            maxEnemies = this.enemys.size();
            for (i = 0; i < maxEnemies; ++i) {
                if (this.nave.intecerpta(this.enemys.get(i)) && !this.nave.mustBeDestroy()) {
                    this.nave.receiveDamage(1.0);
                    this.enemys.get(i).receiveDamage(1.0);
                }
                if ((this.tiempo % 103 == 0 || this.enemys.get((int)i).restanteX == 0.0 && this.enemys.get((int)i).restanteY == 0.0) && !this.enemys.get((int)i).isDestroing) {
                    int minimum = 0;
                    int maximum = this.getWidth();
                    int max_vetical = this.getHeight();
                    int randomNum1 = minimum + (int)(Math.random() * (double)maximum);
                    int randomNum2 = minimum + (int)(Math.random() * (double)max_vetical);
                    this.enemys.get(i).moveTo(new Double(randomNum1), new Double(randomNum2));
                    Laser laser = new Laser(this.enemys.get(i).getX(), this.enemys.get(i).getY(), this.nave.getX(), this.nave.getY(), this.nave);
                    this.proyectilesEnemigos.add(laser);
                }
                if (this.tiempo % (long)((int)(Math.random() * 80.0) + 1) != 0 || this.enemys.get((int)i).isDestroing) continue;
                Laser laser = new Laser(this.enemys.get(i).getX(), this.enemys.get(i).getY(), this.nave.getX(), this.nave.getY(), this.nave);
                this.proyectilesEnemigos.add(laser);
            }
            if (!this.nave.mustBeDestroy()) {
                this.dibuja(this.getGraphics());
            } else {
                JOptionPane.showMessageDialog(this.ventana, "You Lose", "Alerta", 1);
                System.exit(0);
            }
            this.tiempo = System.currentTimeMillis();
        } while (true);
    }

    public void dibuja(Graphics grafico) {
        int i;
        this.pantalla = new BufferedImage(this.getWidth(), this.getHeight(), 1);
        int cantidadEstaticos = this.spritesStaticos.size();
        this.spritesStaticos.get(0).setX(0.0);
        this.spritesStaticos.get(0).setY(0.0);
        for (int i2 = 0; i2 < cantidadEstaticos; ++i2) {
            if (this.spritesStaticos.get(i2).mustBeDestroy()) {
                this.spritesStaticos.remove(i2);
                continue;
            }
            Double X = this.spritesStaticos.get(i2).getX();
            Double Y = this.spritesStaticos.get(i2).getY();
            this.spritesStaticos.get(i2).putSprite(this.pantalla.getGraphics(), X, Y);
        }
        int cantidadDinamicos = this.spritesDinamicos.size();
        for (int i3 = 0; i3 < cantidadDinamicos; ++i3) {
            if (this.spritesDinamicos.get(i3).mustBeDestroy()) {
                this.spritesDinamicos.remove(i3);
                break;
            }
            this.spritesDinamicos.get(i3).move();
            this.spritesDinamicos.get(i3).putSprite(this.pantalla.getGraphics());
        }
        int cantidadProyectiles = this.proyectiles.size();
        for (i = 0; i < cantidadProyectiles; ++i) {
            if (this.proyectiles.get(i).mustBeDestroy()) {
                this.proyectiles.remove(i);
                break;
            }
            this.proyectiles.get(i).move();
            this.proyectiles.get(i).putSprite(this.pantalla.getGraphics());
        }
        cantidadProyectiles = this.proyectilesEnemigos.size();
        for (i = 0; i < cantidadProyectiles; ++i) {
            if (this.proyectilesEnemigos.get(i).mustBeDestroy()) {
                this.proyectilesEnemigos.remove(i);
                break;
            }
            this.proyectilesEnemigos.get(i).move();
            this.proyectilesEnemigos.get(i).putSprite(this.pantalla.getGraphics());
        }
        int cantidadEnemys = this.enemys.size();
        for (int i4 = 0; i4 < cantidadEnemys; ++i4) {
            if (((Sprite)this.enemys.get(i4)).mustBeDestroy()) {
                this.enemys.remove(i4);
                break;
            }
            ((Sprite)this.enemys.get(i4)).move();
            ((Sprite)this.enemys.get(i4)).putSprite(this.pantalla.getGraphics());
        }
        grafico.drawImage(this.pantalla, 0, 0, this);
    }

    public static void main(String[] arg) {
        Juego game = new Juego();
    }

    private class MouseEvent
    implements MouseListener {
        private MouseEvent() {
        }

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (!Juego.this.nave.isIn(new Double(e.getX()), new Double(e.getY())) || e.getModifiers() == 16 || e.getModifiers() == 4 || e.getModifiers() == 8) {
                // empty if block
            }
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            //if (!Juego.access$100((Juego)Juego.this).isDestroing) {
            switch (e.getModifiers()) {
                case 16:
                    Juego.this.nave.rotar(new Double(e.getX()), new Double(e.getY()));
                    Laser laser = new Laser(Juego.this.nave.getX(), Juego.this.nave.getY(), new Double(e.getX()), new Double(e.getY()), Juego.this.nave);
                    Juego.this.proyectiles.add(laser);
                    break;
            // }
                case 4:
                    int x = e.getX();
                    int y = e.getY();
                    int minimoX = Juego.this.nave.getWidth() / 2;
                    int maximoX = Juego.this.pantalla.getWidth() - Juego.this.nave.getWidth() / 2;
                    int minimoY = Juego.this.nave.getHeight() / 2;
                    int maximoY = Juego.this.pantalla.getHeight() - Juego.this.nave.getHeight() / 2 - Juego.this.controles.getHeight() / 2;
                    if (x < minimoX) {
                        x = minimoX;
                    } else if (x > maximoX) {
                        x = maximoX;
                    }
                    if (y < minimoY) {
                        y = minimoY;
                    } else if (y > maximoY) {
                        y = maximoY;
                    }
                    Juego.this.nave.moveTo(new Double(x), new Double(y));
                    Juego.this.marca.setVisible(true);
                    Juego.this.marca.moveTo(new Double(x), new Double(y));
                    break;            
                case 8:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
        }
    }

}

