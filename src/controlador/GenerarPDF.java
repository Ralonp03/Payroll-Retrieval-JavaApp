/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import static com.itextpdf.kernel.pdf.PdfName.Border;
import static com.itextpdf.kernel.pdf.PdfName.Color;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

/**
 *
 * @author Juanc
 */
public class GenerarPDF {
    
    private String Nombre;
    
    private String Apellido;
    
    private String CifEmpresa;
    
    private String NombreEmpresa;
    
    private String IBAN;
    
    private String Categoria;
    
    private String FechaDeAlta;
    
    private String BrutoAnual;
    
    private String SalarioBase;
    
    private String Complemento;
    
    private String TotalDevengos;
    
    private String Desempleo;
    
    private String ContingenciasGenerales;
    
    private String CuotaFormacion;
    
    private String IRPF;
    
    private String DNI;
    
    private String mes;
    
    private String year;
    
    private String rutaImagen;
    
    private String NombrePDF;
    
    private String antiguedadTrienios;
    
    private String porcentajeContingenciasGenerales;
    
    private String numeroContigenciasGenerales;
    
    private String resultadoContigenciasGenerales;
    
    private String porcentajeDesempleo;
    
    private String numeroDesempleo;
    
    private String resultadoDesempleo;
    
    private String porcentajeFormacion;
    
    private String numeroFormacion;
    
    private String resultadoFormacion;
    
    private String porcentajeIRPF;
    
    private String numeroIRPF;
    
    private String resultadoIRPF;
    
    private String prorrateo;
    
    private String totalDeducciones;
    
    private String totalDevengos;
    
    private String liquidoPercibido;
    
    private String calculoEmpresarioBase;
            
    private String porcentajeContingenciasEmpresario;
    
    private String resultadoContingenciasEmpresario;
    
    private String porcentajeDesempleoEmpresario;
    
    private String resultadoDesempleoEmpresario;
    
    private String porcentajeFormacionEmpresario;
    
    private String resultadoFormacionEmpresario;
    
    private String porcentajeAccidenteEmpresario;
    
    private String resultadoAccidenteEmpresario;
    
    private String porcentajeFogasaEmpresario;
    
    private String resultadoFogasaEmpresario;
    
    private String totalEmpresario;
    
    private String totalTrabajador;
    
    private String extra;
    
    public GenerarPDF(String nombreEmpresa,String DNI,String Nombre,String Apellidos,String year,String mes,String CIF,String IBAN,String categoria,String fechaDeAlta,String BrutoAnual,String antiguedadTrienios,String pcg,String ncg,String rcg,String pd,String nd,String rd,String pf,String nf,String rf,String pi,String ni,String ri,String prorrateo,String salarioBase,String complemento,String tdeducciones,String tdevengos,String lp,String ceb,String pce,String rce,String pde,String rde,String pfe,String rfe,String pae,String rae,String pfoe,String rfoe,String tote,String tott,String extra){
        
        this.NombreEmpresa = nombreEmpresa;
        this.DNI = DNI;
        this.Nombre = Nombre;
        this.Apellido = Apellidos;
        this.mes = this.getMes(mes);
        this.year = year;
        StringBuffer cadena1 = new StringBuffer();
        for(int i = 0;i < this.Nombre.length();i++)
        {
            if(this.Nombre.charAt(i) != ' ')
            {
                cadena1.append(this.Nombre.charAt(i));
            }
        }
        this.Nombre = cadena1.toString();
        StringBuffer cadena2 = new StringBuffer();
        for(int i = 0;i < this.Apellido.length();i++)
        {
            if(this.Apellido.charAt(i) != ' ')
            {
                cadena2.append(this.Apellido.charAt(i));
            }
        }
        this.Apellido = cadena2.toString();
        if(extra.equals("SI") == true)
        {
            this.NombrePDF = this.DNI + this.Nombre + this.Apellido + this.mes + this.year + "EXTRA.pdf";  
        }else
        {
            this.NombrePDF = this.DNI + this.Nombre + this.Apellido + this.mes + this.year + ".pdf";
        }
        this.rutaImagen = this.getRutaImagen(nombreEmpresa);
        this.CifEmpresa = CIF;
        this.IBAN = IBAN;
        this.Categoria = categoria;
        this.FechaDeAlta = fechaDeAlta;
        this.BrutoAnual = BrutoAnual;
        this.antiguedadTrienios = antiguedadTrienios;
        this.porcentajeContingenciasGenerales = pcg;
        this.numeroContigenciasGenerales = ncg;
        this.resultadoContigenciasGenerales = rcg;
        this.porcentajeDesempleo = pd;
        this.numeroDesempleo = nd;
        this.resultadoDesempleo = rd;
        this.porcentajeFormacion = pf;
        this.numeroFormacion = nf;
        this.resultadoFormacion = rf;
        this.porcentajeIRPF = pi;
        this.numeroIRPF = ni;
        this.resultadoIRPF = ri;
        this.prorrateo = prorrateo;
        this.SalarioBase = salarioBase;
        this.Complemento = complemento;
        this.totalDeducciones = tdeducciones;
        this.TotalDevengos = tdevengos;
        this.liquidoPercibido = lp;
        this.calculoEmpresarioBase = ceb;
        this.porcentajeContingenciasEmpresario = pce;
        this.resultadoContingenciasEmpresario = rce;
        this.porcentajeDesempleoEmpresario = pde;
        this.resultadoDesempleoEmpresario = rde;
        this.porcentajeFormacionEmpresario = pfe;
        this.resultadoFormacionEmpresario = rfe;
        this.porcentajeAccidenteEmpresario = pae;
        this.resultadoAccidenteEmpresario = rae;
        this.porcentajeFogasaEmpresario = pfoe;
        this.resultadoFogasaEmpresario = rfoe;
        this.totalEmpresario = tote;
        this.totalTrabajador = tott;
        this.extra = extra;
    }
    
    public String getRutaImagen(String nombreEmpresa){
        String aux = "";
        if(nombreEmpresa.equals("PBlankSA") == true)
        {
            aux = "resources/images/PBlankSA.png";
        }else if(nombreEmpresa.equals("APhonSA") == true)
        {
            aux = "resources/images/APhonSA.png";
        }else if(nombreEmpresa.equals("Asoftware") == true)
        {
            aux = "resources/images/Asoftware.png";
        }else if(nombreEmpresa.equals("TecnoLeonSL") == true)
        {
            aux = "resources/images/TecnoLeonSL.png";
        }else if(nombreEmpresa.equals("TecnoProyect") == true)
        {
            aux = "resources/images/TecnoProyect.png";
        }
        return aux;
    }
    
    public String getMes(String mes){
        String aux = "";
        if(mes.charAt(0) == '0')
        {
            StringBuffer cadena = new StringBuffer();
            cadena.append(mes.charAt(0));
            aux = cadena.toString();
        }
        aux = mes;
        
        int mesEntero = Integer.parseInt(aux); 
        
        switch(mesEntero)
        {
            case 1: aux = "Enero";
                    break;
            case 2: aux = "Febrero";
                    break;
            case 3: aux = "Marzo";
                    break;
            case 4: aux = "Abril"; 
                    break;
            case 5: aux = "Mayo";
                    break;
            case 6: aux = "Junio";
                    break;
            case 7: aux = "Julio";
                    break;
            case 8: aux = "Agosto";
                    break;
            case 9: aux = "Septiembre";
                    break;
            case 10: aux = "Octubre";
                    break;
            case 11: aux = "Noviembre";
                    break;
            case 12: aux = "Diciembre";
        }
        return aux;
    }
    
    public void generar() throws FileNotFoundException, MalformedURLException{
        String ruta = "resources/nominas/" + this.NombrePDF;
        PdfWriter writer = new PdfWriter(ruta);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, PageSize.LETTER);

        Paragraph empty = new Paragraph("");
        Table tabla1 = new Table(2);
        tabla1.setWidth(500);

        Paragraph nom = new Paragraph(this.NombreEmpresa);
        Paragraph cif = new Paragraph("CIF: " + this.CifEmpresa);

        Paragraph dir1 = new Paragraph("Avenida de la facultad - 6");
        Paragraph dir2 = new Paragraph("24001 León");

        Cell cell1 = new Cell();
        cell1.setBorder(new SolidBorder(1));
        cell1.setWidth(250);
        cell1.setTextAlignment(TextAlignment.CENTER);

        cell1.add(nom);
        cell1.add(cif);
        cell1.add(dir1);
        cell1.add(dir2);
        tabla1.addCell(cell1);

        Cell cell2 = new Cell();
        cell2.setPadding(10);
        cell2.setTextAlignment(TextAlignment.RIGHT);
        cell2.add(new Paragraph("IBAN: " + this.IBAN));
        cell2.add(new Paragraph("Bruto anual: " + this.BrutoAnual));
        cell2.add(new Paragraph("Categoría: " + this.Categoria));
        cell2.add(new Paragraph("Fecha de alta: " + this.FechaDeAlta));
        tabla1.addCell(cell2);

        Table tabla2 = new Table(2);
        tabla2.setWidth(500);
        Image img = new Image(ImageDataFactory.create(this.rutaImagen));
        img.setPadding(10);
        Cell cell3 = new Cell();
        cell3.add(img);
        cell3.setPaddingLeft(23);
        cell3.setPaddingTop(20);
        
        cell3.setWidth(250);
        tabla2.addCell(cell3);
        
        Cell cell4 = new Cell();
        cell4.setTextAlignment(TextAlignment.RIGHT);
        
        cell4.add(new Paragraph("Destinatario: ").setTextAlignment(TextAlignment.LEFT).setBold());
        cell4.add(new Paragraph(this.Nombre + " " + this.Apellido));
        cell4.add(new Paragraph("DNI: " + this.DNI));
        cell4.add(new Paragraph("Avenida de la facultad"));
        cell4.add(new Paragraph("24001 León"));

        tabla2.addCell(cell4);
        
        Paragraph nominaFecha = new Paragraph();
        if(this.extra.equals("SI") == false)
        {
            nominaFecha = new Paragraph("\nNómina: " + this.mes + " de " + this.year).setBold().setTextAlignment(TextAlignment.CENTER);
        }else
        {
            nominaFecha = new Paragraph("\nNómina: Extra de " + this.mes + " de " + this.year).setBold().setTextAlignment(TextAlignment.CENTER);
        }
        
        final SolidLine lineDrawer = new SolidLine(1f);
        Color color = WebColors.getRGBColor("black"); 
        lineDrawer.setColor(color);
        
        final SolidLine lineDrawerGray = new SolidLine(1f);
        Color colorGray = WebColors.getRGBColor("silver"); 
        lineDrawerGray.setColor(colorGray);
        
        Paragraph titulos = new Paragraph("Conceptos\t\t\t\t\t\t\t\tCantidad\t\t\t            \t\t\t\t\t\t\tDevengo\t\t\tDeduccion");
        titulos.setFontSize(12);
        
        Paragraph primeraTabla = new Paragraph("Salario base\t\t\t\t\t\t\t\t    30 días\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + this.SalarioBase +"\n"
                + "Prorrateo\t\t\t\t\t\t\t\t\t     30 días\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + this.prorrateo + "\n"
                + "Complemento\t\t\t\t\t\t\t\t  30 días\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + this.Complemento + "\n"
                + "Antiguedad\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + this.antiguedadTrienios + "\n"
                + "Contingencias Generales\t\t" + this.porcentajeContingenciasGenerales + "% de " + this.numeroContigenciasGenerales + "  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t " + this.resultadoContigenciasGenerales + "\n"
                + "Desempleo\t\t\t\t\t\t\t  " + this.porcentajeDesempleo + "% de " + this.numeroDesempleo + "  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t " + this.resultadoDesempleo + "\n"
                + "Cuota Formacion\t\t\t\t\t " + this.porcentajeFormacion + "% de " + this.numeroFormacion + "  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t " + this.resultadoFormacion + "\n"
                + "IRPF\t\t\t\t\t\t\t\t\t    " + this.porcentajeIRPF + "% de " + this.numeroIRPF +"  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t " + this.resultadoIRPF + "\n"
        );    
        
        primeraTabla.setFontSize(11);
        
        Paragraph totalDeducciones = new Paragraph("Total Deducciones  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + this.totalDeducciones);
        Paragraph totalDevengos = new Paragraph("Total Devengos\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + this.TotalDevengos);
        
        totalDeducciones.setFontSize(10);
        totalDevengos.setFontSize(10);
        
        Paragraph liquidoPrev = new Paragraph("Liquido a percibir \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + this.liquidoPercibido);
        liquidoPrev.setFontSize(10);
        
        Paragraph saltoLinea = new Paragraph("\n");
        
        Paragraph calcEmpresario = new Paragraph("Calculo empresario: BASE  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + this.calculoEmpresarioBase);
        calcEmpresario.setFontSize(10);
        calcEmpresario.setFontColor(colorGray);
        
        Paragraph segundaTabla = new Paragraph("Contingencias comunes empresario " + this.porcentajeContingenciasEmpresario +  "%\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + this.resultadoContingenciasEmpresario + "\n"
                + "Desempleo " + this.porcentajeDesempleoEmpresario +  "% \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + this.resultadoDesempleoEmpresario + "\n"
                + "Formacion " + this.porcentajeFormacionEmpresario +  "%  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + this.resultadoFormacionEmpresario + "\n"
                + "Accidentes de trabajo " + this.porcentajeAccidenteEmpresario +  "%    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + this.resultadoAccidenteEmpresario + "\n"
                + "FOGASA " + this.porcentajeFogasaEmpresario +  "%    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + this.resultadoFogasaEmpresario + "\n"
        );  
        segundaTabla.setFontSize(10);
        segundaTabla.setFontColor(colorGray);
        
        Paragraph totEmpresario = new Paragraph("Total empresario\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t " + this.totalEmpresario);
        totEmpresario.setFontSize(10);
        totEmpresario.setFontColor(colorGray);
        
        Color colorRed = WebColors.getRGBColor("red"); 
        Paragraph costTotTrabajador = new Paragraph("COSTE TOTAL TRABAJADOR: \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t  " + this.totalTrabajador);
        costTotTrabajador.setBorder(new SolidBorder(1));
        costTotTrabajador.setFontColor(colorRed);
        
        doc.add(tabla1);
        doc.add(tabla2);
        doc.add(nominaFecha);
        doc.add(new LineSeparator(lineDrawer)); //Add linea separadora
        doc.add(titulos);
        doc.add(new LineSeparator(lineDrawer)); //Add linea separadora
        doc.add(primeraTabla);
        doc.add(new LineSeparator(lineDrawer)); //Add linea separadora
        doc.add(totalDeducciones);
        doc.add(totalDevengos);
        doc.add(new LineSeparator(lineDrawer)); //Add linea separadora
        doc.add(liquidoPrev);
        doc.add(saltoLinea);
        doc.add(new LineSeparator(lineDrawerGray)); //Add linea separadora
        doc.add(calcEmpresario);
        doc.add(new LineSeparator(lineDrawerGray)); //Add linea separadora
        doc.add(saltoLinea);
        doc.add(segundaTabla);
        doc.add(new LineSeparator(lineDrawerGray)); //Add linea separadora
        doc.add(totEmpresario);
        doc.add(saltoLinea);
        doc.add(costTotTrabajador);
        doc.close();
    }
}
