package com.artacademy.config;

import com.artacademy.entity.*;
import com.artacademy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        private final ArtWorksRepository artWorksRepository;
        private final ArtWorksCategoryRepository artWorksCategoryRepository;

        private final ArtMaterialsRepository artMaterialsRepository;
        private final ArtMaterialsCategoryRepository artMaterialsCategoryRepository;

        private final ArtClassesRepository artClassesRepository;
        private final ArtClassesCategoryRepository artClassesCategoryRepository;

        private final ArtExhibitionRepository artExhibitionRepository;
        private final ArtExhibitionCategoryRepository artExhibitionCategoryRepository;

        private final ArtGalleryRepository artGalleryRepository;
        private final ArtGalleryCategoryRepository artGalleryCategoryRepository;

        @Override
        @Transactional
        public void run(String... args) throws Exception {
                log.info("--- Starting Application Data Seeding ---");

                // 1. Initialize Essential Roles (System Foundation)
                Map<String, Role> roles = seedRoles();

                // 2. Create Super Admin if not exists
                if (userRepository.findByEmail("admin@artacademy.com").isEmpty()) {
                        seedAdmin(roles.get("ROLE_ADMIN"));
                }

                // 3. Onboard Sample Customers if not enough exist
                // We check for one specific sample user or count to decide
                if (userRepository.findByEmail("alice@test.com").isEmpty()) {
                        seedCustomers(roles.get("ROLE_CUSTOMER"));
                }

                // 4. Seed Art Entities (Each method has its own empty check)
                seedArtWorks();
                seedArtMaterials();
                seedArtClasses();
                seedArtExhibitions();
                seedArtGalleries();

                log.info("--- Data Seeding Completed Successfully ---");
        }

        private Map<String, Role> seedRoles() {
                log.info("[Phase 1] Seeding Roles...");
                List<String> roleNames = List.of("ROLE_CUSTOMER", "ROLE_ADMIN");
                return roleNames.stream()
                                .map(name -> roleRepository.findByName(name)
                                                .orElseGet(() -> roleRepository
                                                                .save(Role.builder().name(name).build())))
                                .collect(Collectors.toMap(Role::getName, role -> role));
        }

        private User seedAdmin(Role adminRole) {
                log.info("[Phase 2] Seeding Super Admin...");
                return userRepository.save(User.builder()
                                .firstName("Super")
                                .lastName("Admin")
                                .email("admin@artacademy.com")
                                .password(passwordEncoder.encode("Password@123"))
                                .phoneNumber("0000000000")
                                .roles(new HashSet<>(Set.of(adminRole.getName())))
                                .isEnabled(true)
                                .build());
        }

        private void seedCustomers(Role customerRole) {
                log.info("[Phase 3] Onboarding Customers...");
                userRepository.save(User.builder()
                                .firstName("Alice")
                                .lastName("Artist")
                                .email("alice@test.com")
                                .password(passwordEncoder.encode("Password@123"))
                                .phoneNumber("1231231234")
                                .roles(new HashSet<>(Set.of(customerRole.getName())))
                                .isEnabled(true)
                                .build());

                userRepository.save(User.builder()
                                .firstName("Bob")
                                .lastName("Collector")
                                .email("bob@test.com")
                                .password(passwordEncoder.encode("Password@123"))
                                .phoneNumber("1231231235")
                                .roles(new HashSet<>(Set.of(customerRole.getName())))
                                .isEnabled(true)
                                .build());

                userRepository.save(User.builder()
                                .firstName("Charlie")
                                .lastName("Critic")
                                .email("charlie@test.com")
                                .password(passwordEncoder.encode("Password@123"))
                                .phoneNumber("1231231236")
                                .roles(new HashSet<>(Set.of(customerRole.getName())))
                                .isEnabled(true)
                                .build());

                userRepository.save(User.builder()
                                .firstName("Diana")
                                .lastName("Designer")
                                .email("diana@test.com")
                                .password(passwordEncoder.encode("Password@123"))
                                .phoneNumber("1231231237")
                                .roles(new HashSet<>(Set.of(customerRole.getName())))
                                .isEnabled(true)
                                .build());

                userRepository.save(User.builder()
                                .firstName("Evan")
                                .lastName("Enthusiast")
                                .email("evan@test.com")
                                .password(passwordEncoder.encode("Password@123"))
                                .phoneNumber("1231231238")
                                .roles(new HashSet<>(Set.of(customerRole.getName())))
                                .isEnabled(true)
                                .build());

                userRepository.save(User.builder()
                                .firstName("Fiona")
                                .lastName("FineArt")
                                .email("fiona@test.com")
                                .password(passwordEncoder.encode("Password@123"))
                                .phoneNumber("1231231239")
                                .roles(new HashSet<>(Set.of(customerRole.getName())))
                                .isEnabled(true)
                                .build());
        }

        private void seedArtWorks() {
                log.info("[Phase 4] Seeding Art Works...");
                if (artWorksRepository.count() > 0)
                        return;

                // Create 6 categories for ArtWorks
                ArtWorksCategory paintingCat = artWorksCategoryRepository.save(ArtWorksCategory.builder()
                                .name("Paintings")
                                .build());

                ArtWorksCategory sculptureCat = artWorksCategoryRepository.save(ArtWorksCategory.builder()
                                .name("Sculptures")
                                .build());

                ArtWorksCategory photographyCat = artWorksCategoryRepository.save(ArtWorksCategory.builder()
                                .name("Photography")
                                .build());

                ArtWorksCategory digitalArtCat = artWorksCategoryRepository.save(ArtWorksCategory.builder()
                                .name("Digital Art")
                                .build());

                ArtWorksCategory mixedMediaCat = artWorksCategoryRepository.save(ArtWorksCategory.builder()
                                .name("Mixed Media")
                                .build());

                ArtWorksCategory printsCat = artWorksCategoryRepository.save(ArtWorksCategory.builder()
                                .name("Prints & Editions")
                                .build());

                // Seed 6 artworks per category
                IntStream.rangeClosed(1, 6).forEach(i -> {
                        artWorksRepository.save(ArtWorks.builder()
                                        .name("Abstract Painting " + i)
                                        .description("A beautiful abstract painting featuring vibrant colors and bold strokes #"
                                                        + i)
                                        .basePrice(BigDecimal.valueOf(1500 + i * 250))
                                        .discountPrice(BigDecimal.valueOf(1400 + i * 250))
                                        .artistName("Maria Santos")
                                        .artMedium("Oil on Canvas")
                                        .size("24x36")
                                        .views(100 + i * 10)
                                        .likes(50 + i * 5)
                                        .categoryId(paintingCat.getId())
                                        .categoryName(paintingCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Painting+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artWorksRepository.save(ArtWorks.builder()
                                        .name("Modern Sculpture " + i)
                                        .description("Contemporary bronze sculpture with abstract human form #" + i)
                                        .basePrice(BigDecimal.valueOf(3500 + i * 500))
                                        .discountPrice(BigDecimal.valueOf(3200 + i * 500))
                                        .artistName("David Chen")
                                        .artMedium("Bronze")
                                        .size("12x12x24")
                                        .views(80 + i * 10)
                                        .likes(40 + i * 5)
                                        .categoryId(sculptureCat.getId())
                                        .categoryName(sculptureCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Sculpture+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artWorksRepository.save(ArtWorks.builder()
                                        .name("Urban Landscape " + i)
                                        .description("Fine art photography capturing city life and architecture #" + i)
                                        .basePrice(BigDecimal.valueOf(800 + i * 150))
                                        .discountPrice(BigDecimal.valueOf(700 + i * 150))
                                        .artistName("Elena Rodriguez")
                                        .artMedium("Digital Print on Canvas")
                                        .size("20x30")
                                        .views(150 + i * 20)
                                        .likes(75 + i * 10)
                                        .categoryId(photographyCat.getId())
                                        .categoryName(photographyCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Photography+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artWorksRepository.save(ArtWorks.builder()
                                        .name("Digital Dreams " + i)
                                        .description("AI-enhanced digital artwork exploring surreal landscapes #" + i)
                                        .basePrice(BigDecimal.valueOf(600 + i * 100))
                                        .discountPrice(BigDecimal.valueOf(500 + i * 100))
                                        .artistName("Alex Kim")
                                        .artMedium("Digital NFT + Print")
                                        .size("4K Resolution")
                                        .views(200 + i * 30)
                                        .likes(100 + i * 15)
                                        .categoryId(digitalArtCat.getId())
                                        .categoryName(digitalArtCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=DigitalArt+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artWorksRepository.save(ArtWorks.builder()
                                        .name("Collage Composition " + i)
                                        .description("Mixed media collage combining vintage papers and acrylics #" + i)
                                        .basePrice(BigDecimal.valueOf(1200 + i * 200))
                                        .discountPrice(BigDecimal.valueOf(1000 + i * 200))
                                        .artistName("Sophie Turner")
                                        .artMedium("Mixed Media on Board")
                                        .size("18x24")
                                        .views(60 + i * 10)
                                        .likes(30 + i * 5)
                                        .categoryId(mixedMediaCat.getId())
                                        .categoryName(mixedMediaCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=MixedMedia+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artWorksRepository.save(ArtWorks.builder()
                                        .name("Limited Edition Print " + i)
                                        .description("Signed and numbered limited edition giclée print #" + i)
                                        .basePrice(BigDecimal.valueOf(350 + i * 50))
                                        .discountPrice(BigDecimal.valueOf(300 + i * 50))
                                        .artistName("Various Artists")
                                        .artMedium("Giclée on Paper")
                                        .size("16x20")
                                        .views(300 + i * 50)
                                        .likes(150 + i * 25)
                                        .categoryId(printsCat.getId())
                                        .categoryName(printsCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Print+" + i)
                                        .build());
                });
        }

        private void seedArtMaterials() {
                log.info("[Phase 5] Seeding Art Materials...");
                if (artMaterialsRepository.count() > 0)
                        return;

                // Create 6 categories for ArtMaterials
                ArtMaterialsCategory brushCat = artMaterialsCategoryRepository.save(ArtMaterialsCategory.builder()
                                .name("Brushes")
                                .build());

                ArtMaterialsCategory paintCat = artMaterialsCategoryRepository.save(ArtMaterialsCategory.builder()
                                .name("Paints")
                                .build());

                ArtMaterialsCategory canvasCat = artMaterialsCategoryRepository.save(ArtMaterialsCategory.builder()
                                .name("Canvases & Surfaces")
                                .build());

                ArtMaterialsCategory drawingCat = artMaterialsCategoryRepository.save(ArtMaterialsCategory.builder()
                                .name("Drawing Supplies")
                                .build());

                ArtMaterialsCategory toolsCat = artMaterialsCategoryRepository.save(ArtMaterialsCategory.builder()
                                .name("Art Tools & Accessories")
                                .build());

                ArtMaterialsCategory paperCat = artMaterialsCategoryRepository.save(ArtMaterialsCategory.builder()
                                .name("Paper & Sketchbooks")
                                .build());

                // Seed 2 materials per category = 12 total
                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artMaterialsRepository.save(ArtMaterials.builder()
                                        .name("Professional Brush Set " + i)
                                        .description("Premium synthetic brushes for oil and acrylic painting, set of 10")
                                        .basePrice(BigDecimal.valueOf(45 + i * 10))
                                        .discount(10)
                                        .stock(BigDecimal.valueOf(100))
                                        .variants(List.of(
                                                        ArtMaterials.MaterialVariant.builder()
                                                                        .id("var-s-" + i)
                                                                        .size("Small Set")
                                                                        .price(BigDecimal.valueOf(45 + i * 10))
                                                                        .discountPrice(BigDecimal.valueOf(40 + i * 10))
                                                                        .stock(BigDecimal.valueOf(50))
                                                                        .build(),
                                                        ArtMaterials.MaterialVariant.builder()
                                                                        .id("var-l-" + i)
                                                                        .size("Large Set")
                                                                        .price(BigDecimal.valueOf(85 + i * 10))
                                                                        .discountPrice(BigDecimal.valueOf(75 + i * 10))
                                                                        .stock(BigDecimal.valueOf(50))
                                                                        .build()))
                                        .categoryId(brushCat.getId())
                                        .categoryName(brushCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Brush+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artMaterialsRepository.save(ArtMaterials.builder()
                                        .name("Acrylic Paint Set " + i)
                                        .description("24-color professional grade acrylic paint set")
                                        .basePrice(BigDecimal.valueOf(65 + i * 15))
                                        .discount(5)
                                        .stock(BigDecimal.valueOf(75))
                                        .categoryId(paintCat.getId())
                                        .categoryName(paintCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Paint+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artMaterialsRepository.save(ArtMaterials.builder()
                                        .name("Stretched Canvas Pack " + i)
                                        .description("Pack of 5 pre-stretched cotton canvases, various sizes")
                                        .basePrice(BigDecimal.valueOf(55 + i * 10))
                                        .discount(0)
                                        .stock(BigDecimal.valueOf(50))
                                        .categoryId(canvasCat.getId())
                                        .categoryName(canvasCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Canvas+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artMaterialsRepository.save(ArtMaterials.builder()
                                        .name("Graphite Pencil Set " + i)
                                        .description("Professional graphite pencils, 2H to 8B, set of 12")
                                        .basePrice(BigDecimal.valueOf(25 + i * 5))
                                        .discount(15)
                                        .stock(BigDecimal.valueOf(120))
                                        .categoryId(drawingCat.getId())
                                        .categoryName(drawingCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Pencil+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artMaterialsRepository.save(ArtMaterials.builder()
                                        .name("Artist Palette Knife Set " + i)
                                        .description("Set of 5 stainless steel palette knives for impasto techniques")
                                        .basePrice(BigDecimal.valueOf(35 + i * 8))
                                        .discount(0)
                                        .stock(BigDecimal.valueOf(60))
                                        .categoryId(toolsCat.getId())
                                        .categoryName(toolsCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Tools+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artMaterialsRepository.save(ArtMaterials.builder()
                                        .name("Watercolor Sketchbook " + i)
                                        .description("A4 cold press watercolor sketchbook, 200gsm, 50 sheets")
                                        .basePrice(BigDecimal.valueOf(20 + i * 5))
                                        .discount(0)
                                        .stock(BigDecimal.valueOf(90))
                                        .categoryId(paperCat.getId())
                                        .categoryName(paperCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Paper+" + i)
                                        .build());
                });
        }

        private void seedArtClasses() {
                log.info("[Phase 6] Seeding Art Classes...");
                if (artClassesRepository.count() > 0)
                        return;

                // Create 6 categories for ArtClasses
                ArtClassesCategory workshopCat = artClassesCategoryRepository.save(ArtClassesCategory.builder()
                                .name("Workshops")
                                .build());

                ArtClassesCategory onlineCat = artClassesCategoryRepository.save(ArtClassesCategory.builder()
                                .name("Online Courses")
                                .build());

                ArtClassesCategory privateCat = artClassesCategoryRepository.save(ArtClassesCategory.builder()
                                .name("Private Lessons")
                                .build());

                ArtClassesCategory groupCat = artClassesCategoryRepository.save(ArtClassesCategory.builder()
                                .name("Group Sessions")
                                .build());

                ArtClassesCategory masterCat = artClassesCategoryRepository.save(ArtClassesCategory.builder()
                                .name("Masterclasses")
                                .build());

                ArtClassesCategory kidsCat = artClassesCategoryRepository.save(ArtClassesCategory.builder()
                                .name("Kids & Teen Classes")
                                .build());

                // Seed 2 classes per category = 12 total
                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artClassesRepository.save(ArtClasses.builder()
                                        .name("Watercolor Weekend Workshop " + i)
                                        .description("Intensive 2-day workshop covering watercolor fundamentals and advanced techniques")
                                        .basePrice(BigDecimal.valueOf(150 + i * 25))
                                        .discountPrice(BigDecimal.valueOf(130 + i * 25))
                                        .durationWeeks(2 + i)
                                        .proficiency("Beginner")
                                        .categoryId(workshopCat.getId())
                                        .categoryName(workshopCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Workshop+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artClassesRepository.save(ArtClasses.builder()
                                        .name("Digital Art Fundamentals " + i)
                                        .description("8-week online course covering digital illustration, tools and techniques")
                                        .basePrice(BigDecimal.valueOf(299 + i * 50))
                                        .discountPrice(BigDecimal.valueOf(269 + i * 50))
                                        .durationWeeks(8)
                                        .proficiency("Intermediate")
                                        .categoryId(onlineCat.getId())
                                        .categoryName(onlineCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Online+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artClassesRepository.save(ArtClasses.builder()
                                        .name("One-on-One Portrait Sessions " + i)
                                        .description("Personalized 2-hour private lessons focused on portrait techniques")
                                        .basePrice(BigDecimal.valueOf(120 + i * 20))
                                        .discountPrice(BigDecimal.valueOf(100 + i * 20))
                                        .durationWeeks(4)
                                        .proficiency("All Levels")
                                        .categoryId(privateCat.getId())
                                        .categoryName(privateCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Private+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artClassesRepository.save(ArtClasses.builder()
                                        .name("Plein Air Painting Group " + i)
                                        .description("Weekly outdoor painting sessions with a group of 8-12 artists")
                                        .basePrice(BigDecimal.valueOf(75 + i * 15))
                                        .discountPrice(BigDecimal.valueOf(65 + i * 15))
                                        .durationWeeks(6)
                                        .proficiency("Intermediate")
                                        .categoryId(groupCat.getId())
                                        .categoryName(groupCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Group+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artClassesRepository.save(ArtClasses.builder()
                                        .name("Oil Painting Masterclass " + i)
                                        .description("Intensive 4-week masterclass with renowned artist, limited to 6 students")
                                        .basePrice(BigDecimal.valueOf(599 + i * 100))
                                        .discountPrice(BigDecimal.valueOf(549 + i * 100))
                                        .durationWeeks(4)
                                        .proficiency("Advanced")
                                        .categoryId(masterCat.getId())
                                        .categoryName(masterCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Master+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artClassesRepository.save(ArtClasses.builder()
                                        .name("Young Artists Program " + i)
                                        .description("Fun and engaging art classes for kids aged 8-15, weekly sessions")
                                        .basePrice(BigDecimal.valueOf(45 + i * 10))
                                        .discountPrice(BigDecimal.valueOf(40 + i * 10))
                                        .durationWeeks(10)
                                        .proficiency("Beginner")
                                        .categoryId(kidsCat.getId())
                                        .categoryName(kidsCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Kids+" + i)
                                        .build());
                });
        }

        private void seedArtExhibitions() {
                log.info("[Phase 7] Seeding Art Exhibitions...");
                if (artExhibitionRepository.count() > 0)
                        return;

                // Create 6 categories for ArtExhibitions
                ArtExhibitionCategory modernCat = artExhibitionCategoryRepository.save(ArtExhibitionCategory.builder()
                                .name("Modern Art")
                                .build());

                ArtExhibitionCategory classicalCat = artExhibitionCategoryRepository
                                .save(ArtExhibitionCategory.builder()
                                                .name("Classical Art")
                                                .build());

                ArtExhibitionCategory contemporaryCat = artExhibitionCategoryRepository
                                .save(ArtExhibitionCategory.builder()
                                                .name("Contemporary")
                                                .build());

                ArtExhibitionCategory photographyCat = artExhibitionCategoryRepository
                                .save(ArtExhibitionCategory.builder()
                                                .name("Photography")
                                                .build());

                ArtExhibitionCategory sculptureCat = artExhibitionCategoryRepository
                                .save(ArtExhibitionCategory.builder()
                                                .name("Sculpture")
                                                .build());

                ArtExhibitionCategory interactiveCat = artExhibitionCategoryRepository
                                .save(ArtExhibitionCategory.builder()
                                                .name("Interactive & Digital")
                                                .build());

                // Seed 2 exhibitions per category = 12 total
                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artExhibitionRepository.save(ArtExhibition.builder()
                                        .name("Future Vision " + i)
                                        .description("An exhibition exploring futuristic themes and abstract expressionism")
                                        .startDate(LocalDate.now().plusDays(i * 10))
                                        .endDate(LocalDate.now().plusDays(i * 10 + 30))
                                        .location("Gallery Hall A-" + i)
                                        .artistCount(12 + i)
                                        .artworksCount(45 + i * 5)
                                        .categoryId(modernCat.getId())
                                        .categoryName(modernCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Modern+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artExhibitionRepository.save(ArtExhibition.builder()
                                        .name("Renaissance Masters " + i)
                                        .description("Celebrating the timeless masterpieces of the Renaissance era")
                                        .startDate(LocalDate.now().plusDays(i * 15))
                                        .endDate(LocalDate.now().plusDays(i * 15 + 45))
                                        .location("Historic Wing " + i)
                                        .artistCount(8 + i)
                                        .artworksCount(30 + i * 3)
                                        .categoryId(classicalCat.getId())
                                        .categoryName(classicalCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Classical+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artExhibitionRepository.save(ArtExhibition.builder()
                                        .name("New Voices " + i)
                                        .description("Emerging artists showcase their provocative contemporary works")
                                        .startDate(LocalDate.now().plusDays(i * 5))
                                        .endDate(LocalDate.now().plusDays(i * 5 + 21))
                                        .location("East Gallery " + i)
                                        .artistCount(20 + i * 2)
                                        .artworksCount(60 + i * 10)
                                        .categoryId(contemporaryCat.getId())
                                        .categoryName(contemporaryCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Contemporary+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artExhibitionRepository.save(ArtExhibition.builder()
                                        .name("Through the Lens " + i)
                                        .description("Documentary and fine art photography from around the world")
                                        .startDate(LocalDate.now().plusDays(i * 20))
                                        .endDate(LocalDate.now().plusDays(i * 20 + 28))
                                        .location("Photo Center " + i)
                                        .artistCount(15 + i)
                                        .artworksCount(80 + i * 10)
                                        .categoryId(photographyCat.getId())
                                        .categoryName(photographyCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Photo+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artExhibitionRepository.save(ArtExhibition.builder()
                                        .name("Form & Space " + i)
                                        .description("Three-dimensional artworks exploring form, texture and space")
                                        .startDate(LocalDate.now().plusDays(i * 12))
                                        .endDate(LocalDate.now().plusDays(i * 12 + 35))
                                        .location("Sculpture Garden " + i)
                                        .artistCount(10 + i)
                                        .artworksCount(25 + i * 5)
                                        .categoryId(sculptureCat.getId())
                                        .categoryName(sculptureCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Sculpture+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artExhibitionRepository.save(ArtExhibition.builder()
                                        .name("Digital Frontiers " + i)
                                        .description("Immersive digital installations and interactive art experiences")
                                        .startDate(LocalDate.now().plusDays(i * 8))
                                        .endDate(LocalDate.now().plusDays(i * 8 + 42))
                                        .location("Tech Pavilion " + i)
                                        .artistCount(8 + i)
                                        .artworksCount(15 + i * 3)
                                        .categoryId(interactiveCat.getId())
                                        .categoryName(interactiveCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Digital+" + i)
                                        .build());
                });
        }

        private void seedArtGalleries() {
                log.info("[Phase 8] Seeding Art Galleries...");
                if (artGalleryRepository.count() > 0)
                        return;

                // Create 6 categories for ArtGalleries
                ArtGalleryCategory downtownCat = artGalleryCategoryRepository.save(ArtGalleryCategory.builder()
                                .name("Downtown")
                                .build());

                ArtGalleryCategory artsDistrictCat = artGalleryCategoryRepository.save(ArtGalleryCategory.builder()
                                .name("Arts District")
                                .build());

                ArtGalleryCategory waterfrontCat = artGalleryCategoryRepository.save(ArtGalleryCategory.builder()
                                .name("Waterfront")
                                .build());

                ArtGalleryCategory historicCat = artGalleryCategoryRepository.save(ArtGalleryCategory.builder()
                                .name("Historic Quarter")
                                .build());

                ArtGalleryCategory suburbanCat = artGalleryCategoryRepository.save(ArtGalleryCategory.builder()
                                .name("Suburban")
                                .build());

                ArtGalleryCategory universityCat = artGalleryCategoryRepository.save(ArtGalleryCategory.builder()
                                .name("University District")
                                .build());

                // Seed 2 galleries per category = 12 total
                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artGalleryRepository.save(ArtGallery.builder()
                                        .name("The Grand Gallery " + i)
                                        .description("Premier art destination in the heart of downtown, featuring world-class exhibitions")
                                        .categoryId(downtownCat.getId())
                                        .categoryName(downtownCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Downtown+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artGalleryRepository.save(ArtGallery.builder()
                                        .name("Studio Collective " + i)
                                        .description("Artist-run gallery space showcasing emerging and mid-career artists")
                                        .categoryId(artsDistrictCat.getId())
                                        .categoryName(artsDistrictCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=ArtsDistrict+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artGalleryRepository.save(ArtGallery.builder()
                                        .name("Harbor View Gallery " + i)
                                        .description("Scenic waterfront gallery specializing in marine and landscape art")
                                        .categoryId(waterfrontCat.getId())
                                        .categoryName(waterfrontCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Waterfront+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artGalleryRepository.save(ArtGallery.builder()
                                        .name("Heritage Art House " + i)
                                        .description("Historic building converted to gallery, focusing on classical and traditional art")
                                        .categoryId(historicCat.getId())
                                        .categoryName(historicCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Historic+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artGalleryRepository.save(ArtGallery.builder()
                                        .name("Community Arts Center " + i)
                                        .description("Family-friendly gallery with educational programs and local artist showcases")
                                        .categoryId(suburbanCat.getId())
                                        .categoryName(suburbanCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=Suburban+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artGalleryRepository.save(ArtGallery.builder()
                                        .name("Campus Art Museum " + i)
                                        .description("University-affiliated museum featuring academic collections and student works")
                                        .categoryId(universityCat.getId())
                                        .categoryName(universityCat.getName())
                                        .imageUrl("https://via.placeholder.com/300?text=University+" + i)
                                        .build());
                });
        }
}
