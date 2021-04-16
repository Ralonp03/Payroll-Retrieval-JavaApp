package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.math.BigDecimal;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class AlgoritmoIBAN {
    AlgoritmoIBAN() {
        super();
    }

    public String ccc(String num) {
        num = "00" + num;

        String izq         = num.substring(0, 10);
        String izqC        = num.substring(10, 11);
        String derC        = num.substring(11, 12);
        String der         = num.substring(12, 22);
        int    controles[] = {
            1, 2, 4, 8, 5, 10, 9, 7, 3, 6
        };
        int    sum         = 0;

        for (int i = 0; i < izq.length(); i++) {
            sum += controles[i] * Integer.valueOf(izq.charAt(i));
        }

        sum = sum % 11;
        sum = 11 - sum;

        if (sum == 10) {
            sum = 1;
        } else if (sum >= 11) {
            sum = 0;
        }

        int sumII = 0;

        for (int i = 0; i < izq.length(); i++) {
            sumII += controles[i] * Integer.valueOf(der.charAt(i));
        }

        sumII = sumII % 11;
        sumII = 11 - sumII;

        if (sumII == 10) {
            sumII = 1;
        } else if (sumII >= 11) {
            sumII = 0;
        }

        boolean error = false;

        if (sum != Integer.valueOf(izqC)) {
            izqC  = String.valueOf(sum);
            error = true;
        }

        if (sumII != Integer.valueOf(derC)) {
            derC  = String.valueOf(sumII);
            error = true;
        }


        return num.substring(2, 10) + izqC + derC + der + ":" + error;
    }

    public String iban(String pais, String num) {
        String numI = "";
        String np   = "";

        for (int i = 0; i < 2; i++) {
            switch (pais.substring(i, i + 1)) {
            case "A" :
                np += "10";

                break;

            case "B" :
                np += "11";

                break;

            case "C" :
                np += "12";

                break;

            case "D" :
                np += "13";

                break;

            case "E" :
                np += "14";

                break;

            case "F" :
                np += "15";

                break;

            case "G" :
                np += "16";

                break;

            case "H" :
                np += "17";

                break;

            case "I" :
                np += "18";

                break;

            case "J" :
                np += "19";

                break;

            case "K" :
                np += "20";

                break;

            case "L" :
                np += "21";

                break;

            case "M" :
                np += "22";

                break;

            case "N" :
                np += "23";

                break;

            case "O" :
                np += "24";

                break;

            case "P" :
                np += "25";

                break;

            case "Q" :
                np += "26";

                break;

            case "R" :
                np += "27";

                break;

            case "S" :
                np += "28";

                break;

            case "T" :
                np += "29";

                break;

            case "U" :
                np += "30";

                break;

            case "V" :
                np += "31";

                break;

            case "W" :
                np += "32";

                break;

            case "X" :
                np += "33";

                break;

            case "Y" :
                np += "34";

                break;

            case "Z" :
                np += "35";

                break;
            }
        }

        numI = num + np + "00";

        BigDecimal n      = new BigDecimal(numI);
        BigDecimal r      = n.remainder(new BigDecimal("97"));
        String     result = String.valueOf(98 - Integer.valueOf(r.toString()));

        if (result.length() == 1) {
            result = "0" + result;
        }

        return pais + result + num;
    }
}


/**
 *
 * @author Ricardo
 */
public class ExcelIBAN {

    /**
     * @param args the command line arguments
     */
    private String localizacionExcel;
    public ExcelIBAN(String localizacionExcel){
        this.localizacionExcel = localizacionExcel;
    }
    public void calculoIBAN(){
        try {

            // Excel
            XSSFWorkbook  workbook = new XSSFWorkbook(new FileInputStream(localizacionExcel));
            AlgoritmoIBAN iban     = new AlgoritmoIBAN();
            XSSFSheet     sheet    = workbook.getSheetAt(2);

            // Archivo XML
            DocumentBuilderFactory dbf   = DocumentBuilderFactory.newInstance();
            DocumentBuilder        db    = dbf.newDocumentBuilder();
            Document               doc   = db.newDocument();
            Element                eRaiz = doc.createElement("Cuentas");

            doc.appendChild(eRaiz);

            // Iterador
            Iterator<Row> rowIterator = sheet.iterator();
            int           counter     = 1;

            for (Row row : sheet) {
                if ((row.getCell(0) != null) &&!row.getCell(2).toString().equals("Categoria")) {

                    // CCC
                    Cell    cell         = row.getCell(9);
                    String  ccc          = cell.toString();
                    String  num          = iban.ccc(ccc);
                    String  numBueno     = num.substring(0, 20);
                    String  comprobacion = num.substring(21);
                    boolean incorrecto   = false;

                    if (comprobacion.equals("true") == true) {
                        cell.setCellValue(numBueno);
                        incorrecto = true;
                    }

                    // IBAN
                    String iii = iban.iban(row.getCell(10).toString(), numBueno);

                    cell = row.getCell(11);

                    if (cell == null) {
                        cell = row.createCell(11);
                    }

                    cell.setCellValue(iii);

                    if (incorrecto) {
                        Element xmlCuentaIDvalue = doc.createElement("Cuenta");

                        eRaiz.appendChild(xmlCuentaIDvalue);

                        Attr atributoIDdeCuenta = doc.createAttribute("id");

                        atributoIDdeCuenta.setValue(String.valueOf(counter));
                        xmlCuentaIDvalue.setAttributeNode(atributoIDdeCuenta);

                        Element nombreUsuarioCuenta = doc.createElement("Nombre");

                        nombreUsuarioCuenta.appendChild(doc.createTextNode(row.getCell(6).toString()));
                        xmlCuentaIDvalue.appendChild(nombreUsuarioCuenta);

                        String apellidos = row.getCell(4).toString();

                        if (row.getCell(5) != null) {
                            apellidos = apellidos + " " + row.getCell(5).toString();
                        }

                        Element apellidosUsuarioCuenta = doc.createElement("Apellidos");

                        apellidosUsuarioCuenta.appendChild(doc.createTextNode(apellidos));
                        xmlCuentaIDvalue.appendChild(apellidosUsuarioCuenta);

                        Element cuentaUsuarioCCCerroneo = doc.createElement("CCCErroneo");

                        cuentaUsuarioCCCerroneo.appendChild(doc.createTextNode(ccc));
                        xmlCuentaIDvalue.appendChild(cuentaUsuarioCCCerroneo);

                        Element cuentaUsuarioEmpresaNombre = doc.createElement("Empresa");

                        cuentaUsuarioEmpresaNombre.appendChild(doc.createTextNode(row.getCell(0).toString()));
                        xmlCuentaIDvalue.appendChild(cuentaUsuarioEmpresaNombre);

                        Element cuentaUsuarioIBANcorrecto = doc.createElement("IBANCorrecto");

                        cuentaUsuarioIBANcorrecto.appendChild(doc.createTextNode(iii));
                        xmlCuentaIDvalue.appendChild(cuentaUsuarioIBANcorrecto);
                    }
                }

                counter++;
            }

            // en el libro de xlsx
            workbook.write(new FileOutputStream(localizacionExcel));
            workbook.close();

            // en el xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer        transformer        = transformerFactory.newTransformer();
            DOMSource          source             = new DOMSource(doc);
            StreamResult       result             = new StreamResult(new File("resources/erroresCCC.xml"));

            transformer.transform(source, result);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            //ex.getCause();
        }
        System.out.println("EXITO: archivo actualizado.");
        
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
