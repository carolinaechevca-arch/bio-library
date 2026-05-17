if (db.books.countDocuments() === 0) {
    db.books.insertMany([
        {
            isbn: '9780132350884',
            title: 'Clean Code: A Handbook of Agile Software Craftsmanship',
            author: 'Robert C. Martin',
            category: 'SOFTWARE_ENGINEERING',
            description: 'Manual de buenas prácticas para escribir código limpio y mantenible.',
            pdfUrl: 'https://www.lkhibra.ma/books/clean-code.pdf',
            imagenUrl: 'https://m.media-amazon.com/images/I/41xShlnTZTL.jpg',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9780470088708',
            title: 'Calculus: Early Transcendentals',
            author: 'James Stewart',
            category: 'MATHEMATICS',
            description: 'Libro base para el estudio de cálculo diferencial e integral.',
            pdfUrl: 'https://fchsmrsneal.wordpress.com/wp-content/uploads/2013/06/calculus-book1.pdf',
            imagenUrl: 'https://www.tbooks.solutions/wp-content/archivos/2013/04/Calculus-Early-Transcendentals-%E2%80%93-James-Stewart-%E2%80%93-6th-Edition.jpg',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9788415552222',
            title: 'Estructuras de Datos en Java',
            author: 'Luis Joyanes Aguilar',
            category: 'COMPUTER_SCIENCE',
            description: 'Conceptos fundamentales de algoritmos y estructuras de datos.',
            pdfUrl: 'https://lc.fie.umich.mx/~a1039048f/nts/Documents/Estructura%20de%20datos%20en%20java%20Joyanes%201ed.pdf',
            imagenUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzRcxYAxdmxZwO08Mv9rrJK77pfYkC6yOsNQ&s',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9781118128169',
            title: 'Operating System Concepts',
            author: 'Abraham Silberschatz',
            category: 'COMPUTER_SCIENCE',
            description: 'La biblia de los sistemas operativos modernos.',
            pdfUrl: 'https://os.ecci.ucr.ac.cr/slides/Abraham-Silberschatz-Operating-System-Concepts-10th-2018.pdf',
            imagenUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTNGmeZ7DiLiksgyLAffFVprkyfagA578cmnQ&s',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9786071503039',
            title: 'Álgebra Lineal',
            author: 'Stanley Grossman',
            category: 'MATHEMATICS',
            description: 'Texto clásico para ingeniería y ciencias básicas.',
            pdfUrl: 'https://aulasvirtuales.udistrital.edu.co/pluginfile.php/774403/mod_resource/content/1/%C3%81lgebra-Lineal-7ma-Edici%C3%B3n-Stanley-l-Grossman.pdf',
            imagenUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSNx3_ddevajQtlol1u3Eb2GagUZ6rrTbq2Jg&s',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9781593276034',
            title: 'Automate the Boring Stuff with Python',
            author: 'Al Sweigart',
            category: 'SOFTWARE_ENGINEERING',
            description: 'Guía práctica para programar tareas cotidianas en Python.',
            pdfUrl: 'https://ia601009.us.archive.org/16/items/automatetheboringstuffwithpython_new/automatetheboringstuffwithpython_new.pdf',
            imagenUrl: 'https://img.dokumen.pub/img/automate-the-boring-stuff-with-python-3rd-edition-early-access-3nbsped-9781718503403-9781718503410.jpg',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9780596517747',
            title: 'JavaScript: The Good Parts',
            author: 'Douglas Crockford',
            category: 'SOFTWARE_ENGINEERING',
            description: 'Descubre las mejores características de JavaScript.',
            pdfUrl: 'https://andersonguelphjs.github.io/OReilly_JavaScript_The_Good_Parts_May_2008.pdf',
            imagenUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQmLadlI7oxqZ5HZKme8l2LXNt2TyWrLZYdxw&s',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9780131103627',
            title: 'The C Programming Language',
            author: 'Brian Kernighan',
            category: 'COMPUTER_SCIENCE',
            description: 'El libro definitivo sobre el lenguaje C.',
            pdfUrl: 'https://www.cimat.mx/ciencia_para_jovenes/bachillerato/libros/%5BKernighan-Ritchie%5DThe_C_Programming_Language.pdf',
            imagenUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgQ0adXqSpi29fq1gJmKpAmzJl7Xqa_Sks0g&s',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9780321127426',
            title: 'Design Patterns',
            author: 'Erich Gamma',
            category: 'SOFTWARE_ENGINEERING',
            description: 'Patrones de diseño orientados a objetos.',
            pdfUrl: 'https://www.javier8a.com/itc/bd1/articulo.pdf',
            imagenUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSS_cyQlwwsHV-2q6ManRSFKctFUW4zBkUJbg&s',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9780262033848',
            title: 'Introduction to Algorithms',
            author: 'Thomas Cormen',
            category: 'COMPUTER_SCIENCE',
            description: 'Referencia exhaustiva sobre algoritmos.',
            pdfUrl: 'https://theswissbay.ch/pdf/Gentoomen%20Library/Algorithms/Introduction%20to%20Algorithms-Cormen.pdf',
            imagenUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSml50mpT1Tg4h29urIAIEnYhH2KBcmHB2B3g&s',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9780073523408',
            title: 'Física para Ciencias e Ingeniería',
            author: 'Raymond Serway',
            category: 'PHYSICS',
            description: 'Fundamentos de física clásica y moderna.',
            pdfUrl: 'https://www2.fisica.unlp.edu.ar/materias/fisgenI/T/Libros/Serway-7Ed.pdf',
            imagenUrl: 'https://m.media-amazon.com/images/I/81JWQ13-pcL._AC_UF1000,1000_QL80_.jpg',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9780321418845',
            title: 'Microeconomía Intermedia',
            author: 'Hal Varian',
            category: 'ECONOMICS',
            description: 'Análisis moderno de la microeconomía.',
            pdfUrl: 'https://api.pageplace.de/preview/DT0400.9789587781205_A43776792/preview-9789587781205_A43776792.pdf',
            imagenUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQXkGLdweUVI2xZ8ImGGmQVYPW_ruXOBQf27A&s',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9780134093413',
            title: 'Campbell Biology',
            author: 'Neil Campbell',
            category: 'BIOLOGY',
            description: 'Texto líder en ciencias biológicas.',
            pdfUrl: 'https://cssplatformbytha.com/wp-content/uploads/2024/10/Biology-by-Neil-A.-Campbell-Jane-B.-Reece-z-lib.org_.pdf',
            imagenUrl: 'https://chemistry.com.pk/wp-content/uploads/2023/08/Campbell-Biology-12e-251x300.webp',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9780073380322',
            title: 'Mecánica de Fluidos',
            author: 'Robert L. Mott',
            category: 'PHYSICS',
            description: 'Principios y aplicaciones de la mecánica de fluidos.',
            pdfUrl: 'https://lumen.uv.mx/resources/files/documents/2022/9/12/7235/b45a600c-e6ca-4cb3-9bc6-1e552a566192.pdf',
            imagenUrl: 'https://www.ingebook.com/ib/pimg/Ingebook/00100_0000002949_6180.jpg',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9780136042594',
            title: 'Inteligencia Artificial: Un Enfoque Moderno',
            author: 'Stuart Russell',
            category: 'COMPUTER_SCIENCE',
            description: 'Teoría y práctica de sistemas inteligentes.',
            pdfUrl: 'https://luismejias21.wordpress.com/wp-content/uploads/2017/09/inteligencia-artificial-un-enfoque-moderno-stuart-j-russell.pdf',
            imagenUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3ccS1tKI2zBXCw04cjvPth0ZxB3FAvMqf6w&s',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9780547167022',
            title: 'Cálculo Integral',
            author: 'Ron Larson',
            category: 'MATHEMATICS',
            description: 'Estudio avanzado de integrales y series.',
            pdfUrl: 'http://www.cobaehtolcayuca.com/LECTURAS/Calculo%20Larsson%208%20edicion.pdf',
            imagenUrl: 'https://isbnmexico.indautor.cerlalc.org/files/titulos/393249.jpg',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9780073511177',
            title: 'Química General',
            author: 'Raymond Chang',
            category: 'CHEMISTRY',
            description: 'Fundamentos químicos para estudiantes de ciencias.',
            pdfUrl: 'https://sacaba.gob.bo/images/wsacaba/pdf/libros/quimica/Chang-QuimicaGeneral7thedicion.pdf',
            imagenUrl: 'https://images.cdn2.buscalibre.com/fit-in/360x360/49/85/4985776b4bed9e4824623a393e06629b.jpg',
            totalLicenses: 5,
            availableLicenses: 5
        },
        {
            isbn: '9780471794714',
            title: 'Estadística para Ingenieros',
            author: 'Douglas Montgomery',
            category: 'MATHEMATICS',
            description: 'Aplicaciones estadísticas en el mundo real.',
            pdfUrl: 'https://ingindustrial869624637.wordpress.com/wp-content/uploads/2019/03/l50.pdf',
            imagenUrl: 'https://elsolucionario.net/wp-content/archivos/2018/03/probabilidad-y-estadistica-aplicadas-a-la-ingenieria-douglas-c-montgomery-2da-edicion.jpg',
            totalLicenses: 5,
            availableLicenses: 5
        }
    ]);
    print('Seed: ' + db.books.countDocuments() + ' books inserted into biolibrary.books');
} else {
    print('Seed: books collection already has data, skipping.');
}
