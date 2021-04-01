/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaspractica1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 *
 * @author Juanc
 */
public class OperacionExcel {
    
    private List<Character> letras = new ArrayList<Character>();
    
    private List nombres = new ArrayList();
    
    private List apellido1 = new ArrayList();
    
    private List apellido2 = new ArrayList();
    
    private List empresa = new ArrayList();
    
    private List categoria = new ArrayList();
    
    public OperacionExcel() throws IOException{
        this.creamosListaLetras();
        this.rellenaLista("Nombre");
        this.rellenaLista("Apellido1");
        this.rellenaLista("Apellido2");
        this.rellenaLista("Nombre empresa");
        this.rellenaLista("Categoria");
    }
    
    public void creamosListaLetras(){
        letras.add('T');
        letras.add('R');
        letras.add('W');
        letras.add('A');
        letras.add('G');
        letras.add('M');
        letras.add('Y');
        letras.add('F');
        letras.add('P');
        letras.add('D');
        letras.add('X');
        letras.add('B');
        letras.add('N');
        letras.add('J');
        letras.add('Z');
        letras.add('S');
        letras.add('Q');
        letras.add('V');
        letras.add('H');
        letras.add('L');
        letras.add('C');
        letras.add('K');
        letras.add('E');    
    }
    
      public void rellenaLista(String campo) throws FileNotFoundException, IOException {
                File f = new File("C:\\Users\\Juanc\\OneDrive\\Documentos\\NetBeansProjects\\SistemasPractica1\\resources\\SistemasInformacionII.xlsx");
                InputStream inp = new FileInputStream(f);
		XSSFWorkbook workBook = new XSSFWorkbook(inp);
		XSSFSheet hs =  workBook.getSheetAt(2);
		
		Iterator rowIter = hs.rowIterator();
		List cellTemp = new ArrayList();
                
                int contador = 1, tope = 0, bloqueo = 0, k = 0, l = 0;
                
		while(rowIter.hasNext()) 
		{
			XSSFRow hr = (XSSFRow) rowIter.next();
			Iterator iterator = hr.cellIterator();
				while(iterator.hasNext())
				{
					XSSFCell hcel = (XSSFCell) iterator.next();
                                        if(hcel.toString().equals(campo) && bloqueo == 0)
                                        {
                                            tope = contador;
                                            bloqueo = 1;
                                        }
                                        if(bloqueo == 1 && k == 1)
                                        {
                                            if(hr.getCell(tope-1) != null && l == 0)
                                            {
                                                cellTemp.add(hr.getCell(tope-1).toString());
                                                l = 1;
                                            }else if(hr.getCell(tope-1) == null && l == 0)
                                            {
                                                cellTemp.add("");
                                                l = 1;
                                            }
                                        }
                                        contador++;
				}
                        k = 1;
                        l = 0;
		}
          if(campo.equals("Nombre"))
          {
                this.nombres = cellTemp;
          }else if(campo.equals("Apellido1"))
          {
                this.apellido1 = cellTemp;
          }else if(campo.equals("Apellido2"))
          {
                this.apellido2 = cellTemp;
          }else if(campo.equals("Nombre empresa"))
          {
                this.empresa = cellTemp;
          }else if(campo.equals("Categoria"))
          {
              this.categoria = cellTemp;
          }
      }
    
    public void leerExcel() throws FileNotFoundException, IOException, ParserConfigurationException, TransformerException {
		File f = new File("C:\\Users\\Juanc\\OneDrive\\Documentos\\NetBeansProjects\\SistemasPractica1\\resources\\SistemasInformacionII.xlsx");
                InputStream inp = new FileInputStream(f);
		XSSFWorkbook workBook = new XSSFWorkbook(inp);
		XSSFSheet hs =  workBook.getSheetAt(2);
                
                List nombresErroneos = new ArrayList<String>();
                List apellido1Erroneos = new ArrayList<String>();
                List apellido2Erroneos = new ArrayList<String>();
                List empresaErroneos = new ArrayList<String>();
                List categoriaErroneos = new ArrayList<String>();
		
		Iterator rowIter = hs.rowIterator();
		List cellTemp = new ArrayList();
                
                int contador = 1, tope = 0, bloqueo = 0, k = 0, l = 0;
                
		while(rowIter.hasNext()) 
		{
			XSSFRow hr = (XSSFRow) rowIter.next();
			Iterator iterator = hr.cellIterator();
				while(iterator.hasNext())
				{
					XSSFCell hcel = (XSSFCell) iterator.next();
                                        if(hcel.toString().equals("NIF/NIE") && bloqueo == 0)
                                        {
                                            tope = contador;
                                            bloqueo = 1;
                                        }
                                        if(bloqueo == 1 && k == 1)
                                        {
                                            if(hr.getCell(tope-1) != null && l == 0)
                                            {
                                                cellTemp.add(hr.getCell(tope-1));
                                                l = 1;
                                            }else if(hr.getCell(tope-1) == null && l == 0)
                                            {
                                                cellTemp.add("");
                                                l = 1;
                                            }
                                        }
                                        contador++;
				}
                        k = 1;
                        l = 0;
		}
        
        int a = 0, suma = 0, id = 0, conta = 0;
        ArrayList<Integer> listaId = new ArrayList<Integer>();
         
         for(int i = 0;i < cellTemp.size();i++)
        {
            if(cellTemp.get(i).toString().equals("") == false && cellTemp.get(i).toString().charAt(0) != 'X' && cellTemp.get(i).toString().charAt(0) != 'Y' && cellTemp.get(i).toString().charAt(0) != 'Z')
            {
             for(int j = 0;j < cellTemp.size();j++)
                {
                if(cellTemp.get(i).toString().equals(cellTemp.get(j).toString()))
                {
                    conta = conta + 1;
       
                    if(conta > 1 && i >= j)
                    {
                        listaId.add(i+2);
                        nombresErroneos.add(this.nombres.get(i));
                        apellido1Erroneos.add(this.apellido1.get(i));
                        if(this.apellido2.get(i).equals("") != true)
                        {
                            apellido2Erroneos.add(this.apellido2.get(i));
                        }else
                        {
                            apellido2Erroneos.add("");
                        }
                        empresaErroneos.add(this.empresa.get(i));
                        categoriaErroneos.add(this.categoria.get(i));
                        }
                }
                }
                
                conta = 0;
                
            for(int j = 0;j < 8;j++)
            {
                a = Character.getNumericValue(cellTemp.get(i).toString().charAt(j));
                suma = suma + a;
         
            }
            if(cellTemp.get(i).toString().charAt(8) != this.devuelveLetra(suma))
            {
                String celda = cellTemp.get(i).toString();
                StringBuffer cadena = new StringBuffer();
                 for(int j = 0;j < 8;j++)
                {
                    cadena.append(cellTemp.get(i).toString().charAt(j));
                }
                cadena.append(this.devuelveLetra(suma));
                this.modificaExcel(celda , cadena.toString());
            }
            suma = 0;
            
            }else if(cellTemp.get(i).toString().equals("") == false)
            {
                for(int j = 0;j < cellTemp.size();j++)
                {
                if(cellTemp.get(i).toString().equals(cellTemp.get(j).toString()))
                {
                    conta = conta + 1;
       
                    if(conta > 1 && i >= j)
                    {
                        listaId.add(i+2);
                        nombresErroneos.add(this.nombres.get(i));
                        apellido1Erroneos.add(this.apellido1.get(i));
                        if(this.apellido2.get(i).equals("") != true)
                        {
                            apellido2Erroneos.add(this.apellido2.get(i));
                        }else
                        {
                            apellido2Erroneos.add("");
                        }
                        empresaErroneos.add(this.empresa.get(i));
                        categoriaErroneos.add(this.categoria.get(i));
                        }
                }
                }
                
                conta = 0;
                
                if(cellTemp.get(i).toString().charAt(0) == 'X' && cellTemp.get(i).toString().charAt(8) != '0' )
                {
                    StringBuffer cadena = new StringBuffer();
                    String celda = cellTemp.get(i).toString();
                    for(int j = 0;j < 8;j++)
                    {
                        cadena.append(cellTemp.get(i).toString().charAt(j));
                    }
                    cadena.append("0");
                    this.modificaExcel(celda , cadena.toString());
                }else if (cellTemp.get(i).toString().charAt(0) == 'Y' && cellTemp.get(i).toString().charAt(8) != '1')
                {
                    StringBuffer cadena = new StringBuffer();
                    String celda = cellTemp.get(i).toString();
                    for(int j = 0;j < 8;j++)
                    {
                        cadena.append(cellTemp.get(i).toString().charAt(j));
                    }
                    cadena.append("1");
                    this.modificaExcel(celda , cadena.toString());
                }else if(cellTemp.get(i).toString().charAt(0) == 'Z' && cellTemp.get(i).toString().charAt(8) != '2')
                {
                    StringBuffer cadena = new StringBuffer();
                    String celda = cellTemp.get(i).toString();
                    for(int j = 0;j < 8;j++)
                    {
                        cadena.append(cellTemp.get(i).toString().charAt(j));
                    }
                    cadena.append("2");
                    this.modificaExcel(celda , cadena.toString());
                }
            }else if(cellTemp.get(i).toString().equals("") == true)
            {
                if(this.nombres.get(i).equals("") != true)
                {
                    id = i+2;
                    listaId.add(id);
                    nombresErroneos.add(this.nombres.get(i));
                    apellido1Erroneos.add(this.apellido1.get(i));
                    if(this.apellido2.get(i).equals("") != true)
                    {
                        apellido2Erroneos.add(this.apellido2.get(i));
                    }else
                    {
                        apellido2Erroneos.add("");
                    }
                    empresaErroneos.add(this.empresa.get(i));
                    categoriaErroneos.add(this.categoria.get(i));
                }
            }
        }
         
        try {
            this.rellenamosXML(listaId,nombresErroneos,apellido1Erroneos,apellido2Erroneos,empresaErroneos,categoriaErroneos);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(OperacionExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(OperacionExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void rellenamosXML(ArrayList<Integer> idList,List<String> nombresErroneos,List<String> apellido1Erroneos,List<String> apellido2Erroneos,List<String> empresasErroneos,List<String> categoriasErroneos) throws ParserConfigurationException, FileNotFoundException, IOException, TransformerConfigurationException, TransformerException{
          // Archivo XML
          
            DocumentBuilderFactory dbf   = DocumentBuilderFactory.newInstance();
            DocumentBuilder        db    = dbf.newDocumentBuilder();
            Document               doc   = db.newDocument();
            Element                eRaiz = doc.createElement("Trabajadores");
            
            doc.appendChild(eRaiz);
            
            for(int i = 0;i < nombresErroneos.size();i++)
            {
            
                        Element xmlCuentaIDvalue = doc.createElement("Trabajador");

                        eRaiz.appendChild(xmlCuentaIDvalue);

                        Attr atributoIDdeCuenta = doc.createAttribute("id");

                        atributoIDdeCuenta.setValue(idList.get(i).toString());
                        xmlCuentaIDvalue.setAttributeNode(atributoIDdeCuenta);

                        Element nombreUsuarioCuenta = doc.createElement("Nombre");
             
                        nombreUsuarioCuenta.appendChild(doc.createTextNode(nombresErroneos.get(i)));
                        xmlCuentaIDvalue.appendChild(nombreUsuarioCuenta);

                        Element apellido1UsuarioCuenta = doc.createElement("PrimerApellido");

                        apellido1UsuarioCuenta.appendChild(doc.createTextNode(apellido1Erroneos.get(i)));
                        xmlCuentaIDvalue.appendChild(apellido1UsuarioCuenta);
                        
                        Element apellido2UsuarioCuenta = doc.createElement("SegundoApellido");

                        apellido2UsuarioCuenta.appendChild(doc.createTextNode(apellido2Erroneos.get(i)));
                        xmlCuentaIDvalue.appendChild(apellido2UsuarioCuenta);

                        Element cuentaUsuarioEmpresaNombre = doc.createElement("Empresa");

                        cuentaUsuarioEmpresaNombre.appendChild(doc.createTextNode(empresasErroneos.get(i)));
                        xmlCuentaIDvalue.appendChild(cuentaUsuarioEmpresaNombre);

                        Element categoria1 = doc.createElement("Categoria");

                        categoria1.appendChild(doc.createTextNode(categoriasErroneos.get(i)));
                        xmlCuentaIDvalue.appendChild(categoria1);
                }
                     
        // en el xml
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer        transformer        = transformerFactory.newTransformer();
        DOMSource          source             = new DOMSource(doc);
        StreamResult       result             = new StreamResult(new File("C:\\Users\\Juanc\\OneDrive\\Documentos\\NetBeansProjects\\SistemasPractica1\\resources\\Errores.xml"));
        
        transformer.transform(source, result);
    }
    
    public char devuelveLetra(int suma){
        int numeroBusqueda = suma % 23;
        //System.out.println(numeroBusqueda);
        return this.letras.get(numeroBusqueda);
    }
    
    public void modificaExcel(String celda,String nueva) throws FileNotFoundException, IOException{
                File f = new File("C:\\Users\\Juanc\\OneDrive\\Documentos\\NetBeansProjects\\SistemasPractica1\\resources\\SistemasInformacionII.xlsx");
                InputStream inp = new FileInputStream(f);
		XSSFWorkbook workBook = new XSSFWorkbook(inp);
		XSSFSheet hs =  workBook.getSheetAt(2);
		
		Iterator rowIter = hs.rowIterator();
                
                int contador = 1, tope = 0, bloqueo = 0, k = 0, l = 0;
                
		while(rowIter.hasNext()) 
		{
			XSSFRow hr = (XSSFRow) rowIter.next();
			Iterator iterator = hr.cellIterator();
				while(iterator.hasNext())
				{
					XSSFCell hcel = (XSSFCell) iterator.next();
                                        if(hcel.toString().equals("NIF/NIE") && bloqueo == 0)
                                        {
                                            tope = contador;
                                            bloqueo = 1;
                                        }
                                        if(bloqueo == 1 && k == 1)
                                        {
                                            if(hr.getCell(tope-1) != null && l == 0 && hr.getCell(tope-1).toString().equals(celda))
                                            {
                                                CellStyle cellStyle = workBook.createCellStyle();
                                                //System.out.println("entro");
                                                hr.getCell(tope-1).setCellValue(nueva);
                                                l = 1;
                                            }
                                        }
                                        contador++;
				}
                        k = 1;
                        l = 0;
		}
        inp.close();
        //Open FileOutputStream to write updates
        FileOutputStream output_file = new FileOutputStream(new File("C:\\Users\\Juanc\\OneDrive\\Documentos\\NetBeansProjects\\SistemasPractica1\\resources\\SistemasInformacionII.xlsx"));
        //write changes
        workBook.write(output_file);
        //close the stream
        output_file.close();            
    }
}
