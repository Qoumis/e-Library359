/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.init;

/**
 *
 * @author mountant
 */
public class Resources {

    static String studentJSON = "{\"username\":\"mountanton\",\"email\":\"mike@csd.uoc.gr\",\"password\":\"ab$12345\","
                + "\"firstname\":\"Michalis\",\"lastname\":\"Mountanton\",\"birthdate\":\"1992-06-03\",\"gender\":\"Male\","
                + "\"student_type\":\"PhD\","
                + "\"student_id\":\"1234567890123\","
                + "\"student_id_from_date\":\"2019-10-10\","
                + "\"student_id_to_date\":\"2023-12-31\","
                + "\"university\":\"UOC\","
                + "\"department\":\"CSD\","
                + "\"country\":\"Greece\",\"city\":\"Heraklion\",\"address\":\"CSD Voutes\",\"lat\":\"35.3053121\","
                + "\"lon\":\"25.0722869\",\"telephone\":\"1234567890\","
                + "\"borrow_count\":\"0\","
                + "\"personalpage\":\"www.mountanton.gr\"}";

     static   String student2JSON = "{\"username\":\"tsitsip\",\"email\":\"tsitsipas@tuc.gr\",\"password\":\"ab$12345\","
                + "\"firstname\":\"Stefanos\",\"lastname\":\"Tsitsipas\",\"birthdate\":\"1998-08-12\",\"gender\":\"Male\","
                + "\"student_type\":\"BSc\","
                + "\"student_id\":\"123412341234\","
                + "\"student_id_from_date\":\"2019-10-10\","
                + "\"student_id_to_date\":\"2024-12-31\","
                + "\"university\":\"TUC\","
                + "\"department\":\"ECE\","
                + "\"country\":\"Greece\",\"city\":\"Chania\",\"address\":\"Venizelou 15\",\"lat\":\"35.5049422\","
                + "\"lon\":\"24.0011885\",\"telephone\":\"6977777777\","
                + "\"borrow_count\":\"0\","
                + "\"personalpage\":\"www.tsitsipas.gr\"}";
    
     
     static String student3JSON = "{\"username\":\"papadaki\",\"email\":\"papadaki@hmu.gr\",\"password\":\"ab$54321\","
                + "\"firstname\":\"Eleni\",\"lastname\":\"Papadaki\",\"birthdate\":\"2002-10-10\",\"gender\":\"Female\","
                + "\"student_type\":\"BSc\","
                + "\"student_id\":\"678967896789\","
                + "\"student_id_from_date\":\"2021-10-10\","
                + "\"student_id_to_date\":\"2026-12-31\","
                + "\"university\":\"HELMEPA\","
                + "\"department\":\"Agro\","
                + "\"country\":\"Greece\",\"city\":\"Heraklion\",\"address\":\"Evans 25\",\"lat\":\"35.3370384\","
                + "\"lon\":\"25.1340107\",\"telephone\":\"2811111111\","
                + "\"borrow_count\":\"0\","
                + "\"personalpage\":\"www.papadakiHelmepa.gr\"}";
    
    static String vikelaia = "{\"username\":\"vikelaia\",\"email\":\"vikelaia@heraklion.com\",\"password\":\"abcdef@1\","
            + "\"firstname\":\"Makis\",\"lastname\":\"Papadakis\",\"birthdate\":\"1970-06-03\",\"gender\":\"Male\","
            + "\"libraryname\":\"Vikelaia\","
            + "\"libraryinfo\":\"Vikelaia Vivliothiki:"
            + "Hours: \n"
            + "Monday	11AM–7PM\n"
            + "Tuesday	8:30AM–2:30PM\n"
            + "Wednesday 11AM–7PM\n"
            + "Thursday	8:30AM–2:30PM\n"
            + "Friday	8:30AM–2:30PM\n"
            + "Saturday	Closed\n"
            + "Sunday	Closed\n"
            + "\","
            + "\"personalpage\":\"https://www.vikelaia.gr/\","
            + "\"country\":\"Greece\",\"city\":\"Heraklion\",\"address\":\"Liontaria Square\",\"lat\":\"35.3389645\","
            + "\"lon\":\"25.1333462\",\"telephone\":\"2813409706\"}";

    static String uocLibraryHER = "{\"username\":\"uoclibher\",\"email\":\"libraryHER@uoc.gr\",\"password\":\"abcdef@2\","
            + "\"firstname\":\"George\",\"lastname\":\"Antoniou\",\"birthdate\":\"1970-12-05\",\"gender\":\"Male\","
            + "\"libraryname\":\"Vivliothiki Panepistimiou Kritis Iraklio\","
            + "\"libraryinfo\":\"Vivliothiki UoC Irakliou:"
            + "Hours: \n"
            + "Monday	8:30AM–8PM\n"
            + "Tuesday	8:30AM–8PM\n"
            + "Wednesday	8:30AM–8PM\n"
            + "Thursday	8:30AM–8PM\n"
            + "Friday	8:30AM–8PM\n"
            + "Saturday	9AM–3PM\n"
            + "Sunday	Closed"
            + "\","
            + "\"country\":\"Greece\",\"city\":\"Heraklion\",\"address\":\"Voutes\",\"lat\":\"35.3070443\","
            + "\"lon\":\"25.0759881\",\"telephone\":\"2810393256\","
            + "\"personalpage\":\"https://www.lib.uoc.gr/\"}";

    static String uocLibraryRETH = "{\"username\":\"uoclibreth\",\"email\":\"libraryRETH@uoc.gr\",\"password\":\"abcdef@3\","
            + "\"firstname\":\"Maria\",\"lastname\":\"Tsitsipa\",\"birthdate\":\"1990-12-05\",\"gender\":\"Female\","
            + "\"libraryname\":\"Vivliothiki Panepistimiou Kritis Rethymno\","
            + "\"libraryinfo\":\"Vivliothiki  UOC Rethymnou:"
            + "Hours: \n"
            + "Monday	8:30AM–8PM"
            + "Tuesday	8:30AM–8PM\n"
            + "Wednesday	8:30AM–8PM\n"
            + "Thursday	8:30AM–8PM\n"
            + "Friday	8:30AM–8PM\n"
            + "Saturday	9AM–3PM\n"
            + "Sunday	Closed\n"
            + "\","
            + "\"country\":\"Greece\",\"city\":\"Rethymno\",\"address\":\"Gallos\",\"lat\":\"35.3036698\","
            + "\"lon\":\"24.4120166\",\"telephone\":\"2813409704\","
            + "\"personalpage\":\"https://www.lib.uoc.gr/\"}";

    static String helmepaLibrary = "{\"username\":\"helmepalib\",\"email\":\"helmepalib@hmu.gr\",\"password\":\"abcdef@4\","
            + "\"firstname\":\"Michalis\",\"lastname\":\"Pappas\",\"birthdate\":\"1973-10-08\",\"gender\":\"Male\","
            + "\"libraryname\":\"Vivliothiki Mesogeiako Panepistimio Iraklio\","
            + "\"libraryinfo\":\"Vivliothiki HELMEPA Irakliou:"
            + "Hours: \n"
            + "Monday	8AM–7PM\n"
            + "Tuesday	8AM–7PM\n"
            + "Wednesday 8AM–7PM\n"
            + "Thursday	8AM–7PM\n"
            + "Friday	8AM–7PM\n"
            + "Saturday	Closed\n"
            + "Sunday	Closed\n"
            + "\","
            + "\"country\":\"Greece\",\"city\":\"Heraklion\",\"address\":\"Panagias Malevi\",\"lat\":\"35.3167396\","
            + "\"lon\":\"25.1029655\",\"telephone\":\"2810379827\","
            + "\"personalpage\":\"https://lib.hmu.gr/en/home\"}";

    static String tucLibrary = "{\"username\":\"tuclib\",\"email\":\"tuclib@tuc.gr\",\"password\":\"abcdef@5\","
            + "\"firstname\":\"Nikos\",\"lastname\":\"Georgakopoulos\",\"birthdate\":\"1990-11-11\",\"gender\":\"Male\","
            + "\"libraryname\":\"Vivliothiki Politexneiou Chania\","
            + "\"libraryinfo\":\"Vivliothiki HELMEPA Chania:"
            + "Hours: \n"
            + "Monday	9AM–7:45PM\n"
            + "Tuesday	9AM–7:45PM\n"
            + "Wednesday 9AM–7:45PM\n"
            + "Thursday	9AM–7:45PM\n"
            + "Friday	9AM–7:45PM\n"
            + "Saturday	Closed\n"
            + "Sunday	Closed\n"
            + "\","
            + "\"country\":\"Greece\",\"city\":\"Chania\",\"address\":\"Kounoupidiana\",\"lat\":\"35.5268188\","
            + "\"lon\":\"24.0695136\",\"telephone\":\"2821037440\","
            + "\"personalpage\":\"https://www.library.tuc.gr\"}";

    static String book1 = "{\"isbn\":\"9780064471046\","
            + "\"title\":\"The Lion, the Witch and the Wardrobe\","
            + "\"authors\":\"C. S. Lewis\","
            + "\"genre\":\"Fantasy\","
            + "\"pages\":\"208\","
            + "\"publicationyear\":\"1950\","
            + "\"url\":\"https://www.abebooks.com/9780064471046/Lion-Witch-Wardrobe-Lewis-0064471047/plp\","
            + "\"photo\":\"https://pictures.abebooks.com/isbn/9780064471046-us.jpg\"}";

    static String book2 = "{\"isbn\":\"9781606801482\","
            + "\"title\":\"She: A History of Adventure\","
            + "\"authors\":\"H. Rider Haggard\","
            + "\"genre\":\"Adventure\","
            + "\"pages\":\"334\","
            + "\"publicationyear\":\"1887\","
            + "\"url\":\"https://www.abebooks.com/products/isbn/9781606801482?cm_sp=bdp-_-ISBN13-_-PLP\","
            + "\"photo\":\"https://pictures.abebooks.com/isbn/9781606801482-us.jpg\"}";

    static String book3 = "{\"isbn\":\"9780439064873\","
            + "\"title\":\"Harry Potter and the Chamber of Secrets\","
            + "\"authors\":\"J.K. Rowling\","
            + "\"genre\":\"Fantasy\","
            + "\"pages\":\"251\","
            + "\"publicationyear\":\"1998\","
            + "\"url\":\"https://www.abebooks.com/products/isbn/9780439064873?cm_sp=bdp-_-ISBN13-_-PLP\","
            + "\"photo\":\"https://pictures.abebooks.com/isbn/9780439064873-us.jpg\"}";
    
     static String book4 = "{\"isbn\":\"9780439136365\","
            + "\"title\":\"Harry Potter and the Prisoner of Azkaban\","
            + "\"authors\":\"J.K. Rowling\","
            + "\"genre\":\"Fantasy\","
            + "\"pages\":\"448\","
            + "\"publicationyear\":\"1999\","
            + "\"url\":\"https://www.abebooks.com/products/isbn/9780439136365/31328751354&cm_sp=snippet-_-srp1-_-PLP1\","
            + "\"photo\":\"https://pictures.abebooks.com/isbn/9780439136365-us.jpg\"}";

     
     static String book5 = "{\"isbn\":\"9780439139601\","
            + "\"title\":\"Harry Potter and the Goblet of Fire\","
            + "\"authors\":\"J.K. Rowling\","
            + "\"genre\":\"Fantasy\","
            + "\"pages\":\"734\","
            + "\"publicationyear\":\"2000\","
            + "\"url\":\"https://www.abebooks.com/products/isbn/9780439139601?cm_sp=bdp-_-ISBN13-_-PLP/\","
            + "\"photo\":\"https://pictures.abebooks.com/isbn/9780439139601-us.jpg\"}";
     
     static String book6 = "{\"isbn\":\"9781451681758\","
            + "\"title\":\"The Light Between Oceans\","
            + "\"authors\":\"M.L. Stedman\","
            + "\"genre\":\"Novel\","
            + "\"pages\":\"362\","
            + "\"publicationyear\":\"2012\","
            + "\"url\":\"https://www.abebooks.com/products/isbn/9781451681758/31284736033&cm_sp=snippet-_-srp1-_-PLP1\","
            + "\"photo\":\"https://pictures.abebooks.com/isbn/9781451681758-us.jpg\"}";
     
     static String book7 = "{\"isbn\":\"9780743273565\","
            + "\"title\":\"The Great Gatsby\","
            + "\"authors\":\"F. Scott Fitzgerald.\","
            + "\"genre\":\"Novel\","
            + "\"pages\":\"208\","
            + "\"publicationyear\":\"1925\","
            + "\"url\":\"https://www.abebooks.com/products/isbn/9780743273565?cm_sp=bdp-_-ISBN13-_-PLP\","
            + "\"photo\":\"https://pictures.abebooks.com/isbn/9780743273565-us.jpg\"}";
     
       static String book8 = "{\"isbn\":\"9781538330456\","
            + "\"title\":\"Giannis Antetokounmpo (Young Sports Greats)\","
            + "\"authors\":\"Sarah Machajewski\","
            + "\"genre\":\"Sports\","
            + "\"pages\":\"24\","
            + "\"publicationyear\":\"2018\","
            + "\"url\":\"https://www.abebooks.com/products/isbn/9781538330456?cm_sp=bdp-_-ISBN13-_-PLP\","
            + "\"photo\":\"https://pictures.abebooks.com/isbn/9781538330456-us.jpg\"}";
       
       
         static String book9 = "{\"isbn\":\"9780446676090\","
            + "\"title\":\"The Notebook\","
            + "\"authors\":\"Nicholas Sparks\","
            + "\"genre\":\"Romance, Novel\","
            + "\"pages\":\"214\","
            + "\"publicationyear\":\"1996\","
            + "\"url\":\"https://www.abebooks.com/products/isbn/9780446676090?cm_sp=bdp-_-ISBN13-_-PLP\","
            + "\"photo\":\"https://pictures.abebooks.com/isbn/9780446676090-us.jpg\"}";

         
         static String book10 = "{\"isbn\":\"9780451457813\","
            + "\"title\":\"Storm Front\","
            + "\"authors\":\"Jim Butcher\","
            + "\"genre\":\"Fantasy, Novel\","
            + "\"pages\":\"322\","
            + "\"publicationyear\":\"2000\","
            + "\"url\":\"https://www.abebooks.com/products/isbn/9780451457813?cm_sp=bdp-_-ISBN13-_-PLP\","
            + "\"photo\":\"https://pictures.abebooks.com/isbn/9780451457813-us.jpg\"}";
}
