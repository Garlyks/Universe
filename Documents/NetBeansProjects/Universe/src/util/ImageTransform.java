package util;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageTransform {
    private AffineTransform at = new AffineTransform();
    private int alturaImagen;
    private int anchoImagen;
    private double grados;

    public ImageTransform(int alturaImagen, int anchuraImagen) {
        this.alturaImagen = alturaImagen;
        this.anchoImagen = anchuraImagen;
    }

    public AffineTransform getTransform() {
        return this.at;
    }

    public void rotate(double grados) {
        this.grados = grados;
        this.at.rotate(Math.toRadians(grados), (double)this.anchoImagen / 2.0, (double)this.alturaImagen / 2.0);
    }

    public void findTranslation() {
        Point2D p2din = this.hallarPtoATraslacion();
        Point2D p2dout = this.at.transform(p2din, null);
        double ytrans = p2dout.getY();
        p2din = this.hallarPtoBTraslacion();
        p2dout = this.at.transform(p2din, null);
        double xtrans = p2dout.getX();
        AffineTransform tat = new AffineTransform();
        tat.translate(- xtrans, - ytrans);
        this.at.preConcatenate(tat);
    }

    private Point2D hallarPtoATraslacion() {
        Point2D.Double p2din = this.grados >= 0.0 && this.grados <= 90.0 ? new Point2D.Double(0.0, 0.0) : (this.grados > 90.0 && this.grados <= 180.0 ? new Point2D.Double(0.0, this.alturaImagen) : (this.grados > 180.0 && this.grados <= 270.0 ? new Point2D.Double(this.anchoImagen, this.alturaImagen) : new Point2D.Double(this.anchoImagen, 0.0)));
        return p2din;
    }

    private Point2D hallarPtoBTraslacion() {
        Point2D.Double p2din = this.grados >= 0.0 && this.grados <= 90.0 ? new Point2D.Double(0.0, this.alturaImagen) : (this.grados > 90.0 && this.grados <= 180.0 ? new Point2D.Double(this.anchoImagen, this.alturaImagen) : (this.grados > 180.0 && this.grados <= 270.0 ? new Point2D.Double(this.anchoImagen, 0.0) : new Point2D.Double(0.0, 0.0)));
        return p2din;
    }

    public static BufferedImage rotacionImagen(BufferedImage origen, double grados) {
        ImageTransform imTransform = new ImageTransform(origen.getHeight(), origen.getWidth());
        imTransform.rotate(grados);
        imTransform.findTranslation();
        AffineTransformOp ato = new AffineTransformOp(imTransform.getTransform(), 2);
        BufferedImage destinationImage = ato.createCompatibleDestImage(origen, origen.getColorModel());
        return ato.filter(origen, destinationImage);
    }

    private void saveJPGImage(BufferedImage im) throws IOException {
        ImageIO.write((RenderedImage)im, "JPG", new File("imagen.jpg"));
    }
}

