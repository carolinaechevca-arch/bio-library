package com.bio_library.catalog.infrastructure.configuration;

import com.bio_library.catalog.domain.constants.DomainConstants;
import com.bio_library.catalog.domain.enums.Category;
import com.bio_library.catalog.domain.enums.Language;
import com.bio_library.catalog.domain.enums.LicenseType;
import com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document.AuthorDocument;
import com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document.BookDocument;
import com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document.LicenseDocument;
import com.bio_library.catalog.infrastructure.adapters.driven.mongodb.repository.IBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final IBookRepository bookRepository;

    @Override
    public void run(String... args) {
        if (bookRepository.count() > 0) {
            log.info("[INIT] Books collection already has data, skipping initialization");
            return;
        }
        log.info("[INIT] Initializing books collection with sample data");
        bookRepository.saveAll(buildBooks());
        log.info("[INIT] Books collection initialized with {} books", bookRepository.count());
    }

    private List<BookDocument> buildBooks() {
        return List.of(
                book("978-0-13-468599-1", "Clean Code", "Robert", "Martin",
                        Category.SOFTWARE_ENGINEERING, "Prentice Hall", 2008, 1, Language.ENGLISH,
                        "A handbook of agile software craftsmanship.", 431, DomainConstants.MAX_CONCURRENT_LOANS),
                book("978-0-201-63361-0", "Design Patterns", "Erich", "Gamma",
                        Category.SOFTWARE_ENGINEERING, "Addison-Wesley", 1994, 1, Language.ENGLISH,
                        "Elements of reusable object-oriented software.", 395, DomainConstants.MAX_CONCURRENT_LOANS),
                book("978-0-13-235088-4", "The Pragmatic Programmer", "Andrew", "Hunt",
                        Category.SOFTWARE_ENGINEERING, "Addison-Wesley", 1999, 1, Language.ENGLISH,
                        "Your journey to mastery in software development.", 321, DomainConstants.MAX_CONCURRENT_LOANS),
                book("978-0-596-51774-8", "JavaScript: The Good Parts", "Douglas", "Crockford",
                        Category.SOFTWARE_ENGINEERING, "O'Reilly", 2008, 1, Language.ENGLISH,
                        "Unearthing the excellence in JavaScript.", 172, DomainConstants.MAX_CONCURRENT_LOANS),
                book("978-0-13-110362-7", "The C Programming Language", "Brian", "Kernighan",
                        Category.SOFTWARE_ENGINEERING, "Prentice Hall", 1988, 2, Language.ENGLISH,
                        "The definitive reference for the C programming language.", 272, DomainConstants.MAX_CONCURRENT_LOANS),
                book("978-3-16-148410-0", "Introduction to Algorithms", "Thomas", "Cormen",
                        Category.MATHEMATICS, "MIT Press", 2009, 3, Language.ENGLISH,
                        "A comprehensive introduction to algorithms.", 1292, DomainConstants.MAX_CONCURRENT_LOANS),
                book("978-0-07-296634-9", "Calculus", "James", "Stewart",
                        Category.MATHEMATICS, "Brooks Cole", 2015, 8, Language.ENGLISH,
                        "Complete calculus course covering single and multivariable calculus.", 1368, DomainConstants.MAX_CONCURRENT_LOANS),
                book("978-0-201-31452-6", "The Art of Computer Programming", "Donald", "Knuth",
                        Category.SOFTWARE_ENGINEERING, "Addison-Wesley", 2011, 1, Language.ENGLISH,
                        "Comprehensive monograph written by Donald Knuth.", 896, DomainConstants.MAX_CONCURRENT_LOANS),
                book("978-1-491-91205-8", "Designing Data-Intensive Applications", "Martin", "Kleppmann",
                        Category.SOFTWARE_ENGINEERING, "O'Reilly", 2017, 1, Language.ENGLISH,
                        "The big ideas behind reliable, scalable, and maintainable systems.", 544, DomainConstants.MAX_CONCURRENT_LOANS),
                book("978-0-13-468599-2", "Refactoring", "Martin", "Fowler",
                        Category.SOFTWARE_ENGINEERING, "Addison-Wesley", 2018, 2, Language.ENGLISH,
                        "Improving the design of existing code.", 448, DomainConstants.MAX_CONCURRENT_LOANS)
        );
    }

    private BookDocument book(String isbn, String title, String authorName, String authorLastName,
                               Category category, String publisher, int year, int edition,
                               Language language, String synopsis, int pages, int maxLoans) {
        BookDocument doc = new BookDocument();
        doc.setIsbn(isbn);
        doc.setTitle(title);

        AuthorDocument author = new AuthorDocument();
        author.setName(authorName);
        author.setLastName(authorLastName);
        doc.setAuthor(author);

        doc.setCategory(category);
        doc.setPublisher(publisher);
        doc.setYear(year);
        doc.setEdition(edition);
        doc.setLanguage(language);
        doc.setSynopsis(synopsis);
        doc.setPages(pages);

        String slug = title.toLowerCase().replace(" ", "-").replace(":", "").replace("'", "");
        doc.setPdfUrl("https://storage.googleapis.com/biolibrary/pdfs/" + slug + ".pdf");
        doc.setCoverImageUrl("https://storage.googleapis.com/biolibrary/covers/" + slug + ".jpg");

        LicenseDocument license = new LicenseDocument();
        license.setMaxConcurrentLoans(maxLoans);
        license.setActiveLoanCount(0);
        doc.setLicense(license);

        doc.setActive(true);

        return doc;
    }
}
