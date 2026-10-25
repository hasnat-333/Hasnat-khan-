package com.example.data

import com.example.data.model.AgencySettings
import com.example.data.model.PortfolioProject
import com.example.data.model.ServiceItem
import com.example.data.model.ServicePackage
import com.example.data.model.TestimonialItem

object DefaultData {

    val initialSettings = AgencySettings(
        id = "general",
        brandName = "Hasnat Khan Creative Services",
        tagline = "Your Vision. Our Creativity. Professional Results.",
        whatsAppNumber = "+923135526057",
        email = "hasnat.khan8238@gmail.com",
        businessHours = "Mon - Sat: 9:00 AM - 10:00 PM (PKT)",
        currency = "PKR",
        bannerText = "⚡ Fast turnaround on all Logo, Web & Video projects! Contact via WhatsApp for immediate quotes."
    )

    val defaultServices = listOf(
        ServiceItem(
            id = "srv_logo_design",
            serviceId = "srv_logo_design",
            title = "Logo Design",
            slug = "logo-design",
            category = "Graphic Design",
            description = "Custom minimalist, modern, and luxury logo design crafted to give your brand a memorable, professional identity.",
            startingPrice = 4500L,
            deliveryTime = "2-3 Days",
            iconName = "brush",
            isFeatured = true,
            isActive = true,
            packages = mapOf(
                "Basic" to ServicePackage(
                    name = "Basic",
                    price = 4500L,
                    description = "2 Initial concepts, high-res PNG & JPG, transparent file",
                    deliveryTime = "2 Days",
                    revisions = "3 Revisions",
                    features = listOf("2 Unique Logo Concepts", "High Resolution PNG/JPG", "Transparent Background", "Color Palette Guide")
                ),
                "Standard" to ServicePackage(
                    name = "Standard",
                    price = 8500L,
                    description = "3 Initial concepts, vector source files (AI/EPS/SVG), 3D mockup",
                    deliveryTime = "3 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("3 Unique Logo Concepts", "Vector Source Files (AI, SVG, PDF)", "3D Realistic Mockup", "Social Media Display Kit", "Unlimited Revisions")
                ),
                "Premium" to ServicePackage(
                    name = "Premium",
                    price = 15000L,
                    description = "Full brand identity kit, stationery design, copyright transfer & priority support",
                    deliveryTime = "4 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("5 Premium Logo Concepts", "Full Vector & Print-Ready Files", "Complete Brand Guidelines Document", "Stationery Design (Business Card + Letterhead)", "Social Media Kit (All Platforms)", "VIP 24/7 Support")
                )
            )
        ),
        ServiceItem(
            id = "srv_graphic_design",
            serviceId = "srv_graphic_design",
            title = "Graphic Design",
            slug = "graphic-design",
            category = "Graphic Design",
            description = "High-impact marketing graphics, brochures, flyers, brand collateral, and custom visual assets.",
            startingPrice = 3500L,
            deliveryTime = "1-2 Days",
            iconName = "palette",
            isFeatured = true,
            isActive = true,
            packages = mapOf(
                "Basic" to ServicePackage(
                    name = "Basic",
                    price = 3500L,
                    description = "Single-sided design (flyer/card), print-ready CMYK format",
                    deliveryTime = "1 Day",
                    revisions = "2 Revisions",
                    features = listOf("1 Custom Graphic Design", "Print Ready CMYK PDF", "High-Res JPEG", "2 Revisions")
                ),
                "Standard" to ServicePackage(
                    name = "Standard",
                    price = 6500L,
                    description = "Double-sided flyer/brochure or 2 creative banners with editable source files",
                    deliveryTime = "2 Days",
                    revisions = "5 Revisions",
                    features = listOf("Double-Sided Design / 2 Variations", "Print-Ready & Web Formats", "Editable Source File (PSD/AI)", "Commercial Use License")
                ),
                "Premium" to ServicePackage(
                    name = "Premium",
                    price = 12000L,
                    description = "Comprehensive multi-page brochure or complete marketing collateral bundle",
                    deliveryTime = "3-4 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("Multi-page Brochure / Catalog (up to 6 pages)", "Full Vector & Photoshop Assets", "Custom Illustrations & Icons", "Print Prep & Color Proofing")
                )
            )
        ),
        ServiceItem(
            id = "srv_video_editing",
            serviceId = "srv_video_editing",
            title = "Video Editing",
            slug = "video-editing",
            category = "Video & Animation",
            description = "Cinematic video editing, Reels/TikToks, YouTube content, color grading, sound design, and motion graphics.",
            startingPrice = 5000L,
            deliveryTime = "2-3 Days",
            iconName = "movie",
            isFeatured = true,
            isActive = true,
            packages = mapOf(
                "Basic" to ServicePackage(
                    name = "Basic",
                    price = 5000L,
                    description = "Up to 60-second Short / Reel / TikTok with subtitles, music & fast pacing",
                    deliveryTime = "1-2 Days",
                    revisions = "2 Revisions",
                    features = listOf("Up to 60s Video", "Dynamic Captions / Subtitles", "Royalty-free Background Music", "Smooth Transitions & Zooms")
                ),
                "Standard" to ServicePackage(
                    name = "Standard",
                    price = 10000L,
                    description = "Up to 5 minutes YouTube / promo video with color grading, B-roll & sound FX",
                    deliveryTime = "3 Days",
                    revisions = "4 Revisions",
                    features = listOf("Up to 5 Min Edited Video", "Advanced Sound Design & EQ", "Cinematic Color Grading", "B-roll & Motion Graphics Integration", "Thumbnail Concept Included")
                ),
                "Premium" to ServicePackage(
                    name = "Premium",
                    price = 22000L,
                    description = "Full commercial / documentary (up to 15 mins) with custom motion graphics & 4K master",
                    deliveryTime = "5 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("Up to 15 Min 4K Master Video", "Custom Animated Title Cards", "Multi-cam Sync & Advanced Audio Cleanup", "Social Teaser Cuts Included", "Project File Handover")
                )
            )
        ),
        ServiceItem(
            id = "srv_web_development",
            serviceId = "srv_web_development",
            title = "Website Development",
            slug = "website-development",
            category = "Web Development",
            description = "Modern, high-converting, responsive websites, landing pages, and business portals engineered for speed and SEO.",
            startingPrice = 25000L,
            deliveryTime = "5-7 Days",
            iconName = "code",
            isFeatured = true,
            isActive = true,
            packages = mapOf(
                "Basic" to ServicePackage(
                    name = "Basic",
                    price = 25000L,
                    description = "High-converting single-page landing page with WhatsApp CTA & mobile responsiveness",
                    deliveryTime = "4 Days",
                    revisions = "3 Revisions",
                    features = listOf("1-Page High-Conversion Landing Page", "Mobile & Tablet Responsive", "WhatsApp & Contact Integration", "Fast Load Speed Optimization")
                ),
                "Standard" to ServicePackage(
                    name = "Standard",
                    price = 45000L,
                    description = "Complete 5-page corporate website (Home, Services, About, Portfolio, Contact)",
                    deliveryTime = "7 Days",
                    revisions = "5 Revisions",
                    features = listOf("Up to 5 Custom Pages", "Modern UI/UX Design System", "SEO Friendly Metadata Setup", "Contact Forms & Lead Capture", "1 Month Free Maintenance")
                ),
                "Premium" to ServicePackage(
                    name = "Premium",
                    price = 80000L,
                    description = "Full custom dynamic website or e-commerce booking platform with admin dashboard",
                    deliveryTime = "12-14 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("Multi-page Dynamic Web Application", "Content Management / Admin Dashboard", "Payment Gateway & Booking Engine", "Database Integration", "3 Months Ongoing Support")
                )
            )
        ),
        ServiceItem(
            id = "srv_social_media",
            serviceId = "srv_social_media",
            title = "Social Media Post Design",
            slug = "social-media-post-design",
            category = "Social Media",
            description = "Engaging Instagram, Facebook, and LinkedIn post designs and carousels that drive engagement and sales.",
            startingPrice = 3000L,
            deliveryTime = "1-2 Days",
            iconName = "share",
            isFeatured = false,
            isActive = true,
            packages = mapOf(
                "Basic" to ServicePackage(
                    name = "Basic",
                    price = 3000L,
                    description = "3 High quality branded social media posts with matching captions",
                    deliveryTime = "1 Day",
                    revisions = "2 Revisions",
                    features = listOf("3 Custom Designed Posts", "Square & Story Formats", "Branded Color Scheme", "2 Revisions")
                ),
                "Standard" to ServicePackage(
                    name = "Standard",
                    price = 7000L,
                    description = "8 Posts pack or 2 multi-slide educational carousels",
                    deliveryTime = "3 Days",
                    revisions = "4 Revisions",
                    features = listOf("8 Custom Social Posts OR 2 Carousels", "Canva / PSD Editable Templates", "Hashtag & Copy Strategy Notes", "Highlight Covers Included")
                ),
                "Premium" to ServicePackage(
                    name = "Premium",
                    price = 15000L,
                    description = "Monthly 20-post social media campaign bundle with stories & banners",
                    deliveryTime = "5 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("20 Branded Social Posts & Stories", "Content Calendar Layout", "Editable Master Templates", "Profile Bio & Banner Optimization")
                )
            )
        ),
        ServiceItem(
            id = "srv_yt_thumbnail",
            serviceId = "srv_yt_thumbnail",
            title = "YouTube Thumbnail Design",
            slug = "youtube-thumbnail-design",
            category = "Social Media",
            description = "High Click-Through-Rate (CTR) custom YouTube thumbnails designed to boost views and channel subscribers.",
            startingPrice = 2000L,
            deliveryTime = "24 Hours",
            iconName = "play_circle",
            isFeatured = false,
            isActive = true,
            packages = mapOf(
                "Basic" to ServicePackage(
                    name = "Basic",
                    price = 2000L,
                    description = "1 Eye-catching high CTR YouTube thumbnail",
                    deliveryTime = "24 Hours",
                    revisions = "3 Revisions",
                    features = listOf("1 Custom Click-Worthy Thumbnail", "1920x1080 Full HD", "Face Enhancement & Lighting FX", "Fast 24-Hour Delivery")
                ),
                "Standard" to ServicePackage(
                    name = "Standard",
                    price = 5000L,
                    description = "3 Custom thumbnails with A/B testing variations",
                    deliveryTime = "1-2 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("3 Unique Thumbnails / A-B Variants", "Editable PSD Source Files", "3D Text & Glow Effects", "Unlimited Revisions")
                ),
                "Premium" to ServicePackage(
                    name = "Premium",
                    price = 9000L,
                    description = "6 Premium thumbnails bundle for active YouTubers",
                    deliveryTime = "3 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("6 Custom Thumbnails Pack", "Channel Banner Artwork Included", "Priority 12-Hour Rush Turnaround", "Source Files & Assets Included")
                )
            )
        ),
        ServiceItem(
            id = "srv_photo_editing",
            serviceId = "srv_photo_editing",
            title = "Photo Editing",
            slug = "photo-editing",
            category = "Graphic Design",
            description = "Professional portrait retouching, product photo editing for e-commerce, background removal, and color manipulation.",
            startingPrice = 2500L,
            deliveryTime = "24 Hours",
            iconName = "photo_camera",
            isFeatured = false,
            isActive = true,
            packages = mapOf(
                "Basic" to ServicePackage(
                    name = "Basic",
                    price = 2500L,
                    description = "5 Product photos background removal & clean-up",
                    deliveryTime = "24 Hours",
                    revisions = "2 Revisions",
                    features = listOf("5 Images Background Removal", "Pure White / Transparent Background", "Drop Shadow Addition", "Crop & Center Alignment")
                ),
                "Standard" to ServicePackage(
                    name = "Standard",
                    price = 5500L,
                    description = "10 Photos advanced retouching, skin smoothing, and color correction",
                    deliveryTime = "2 Days",
                    revisions = "4 Revisions",
                    features = listOf("10 Images High-End Retouching", "Frequency Separation Skin Tone", "Object Removal & Clean-up", "High-Resolution Output")
                ),
                "Premium" to ServicePackage(
                    name = "Premium",
                    price = 11000L,
                    description = "25 E-commerce catalog photos with realistic reflections & Amazon/Daraz optimization",
                    deliveryTime = "3 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("25 Photos Full E-Commerce Suite", "Daraz / Amazon Compliant Specs", "Color Grading & Highlights", "Bulk Source Files")
                )
            )
        ),
        ServiceItem(
            id = "srv_business_branding",
            serviceId = "srv_business_branding",
            title = "Business Branding",
            slug = "business-branding",
            category = "Branding",
            description = "End-to-end corporate identity: Brand style guide, color psychology, typography rules, business stationery, and social kits.",
            startingPrice = 18000L,
            deliveryTime = "5-7 Days",
            iconName = "business_center",
            isFeatured = true,
            isActive = true,
            packages = mapOf(
                "Basic" to ServicePackage(
                    name = "Basic",
                    price = 18000L,
                    description = "Logo, color palette, typography selection & business card design",
                    deliveryTime = "4 Days",
                    revisions = "4 Revisions",
                    features = listOf("Logo Suite (Primary & Submark)", "Official Color Palette & Font Pairings", "Double-Sided Business Card", "Email Signature Graphic")
                ),
                "Standard" to ServicePackage(
                    name = "Standard",
                    price = 32000L,
                    description = "Complete corporate identity kit: Stationery, brand guidelines book, and invoice templates",
                    deliveryTime = "7 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("Full Brand Guidelines Book (PDF)", "Business Card, Letterhead & Envelope", "Social Media Cover Kit", "Invoice & Presentation Template", "Unlimited Revisions")
                ),
                "Premium" to ServicePackage(
                    name = "Premium",
                    price = 60000L,
                    description = "Enterprise brand architecture: Packaging design, merchandise, 3D mockups & brand storytelling",
                    deliveryTime = "10 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("Comprehensive 25+ Page Brand Book", "Product Packaging / Label Mockups", "Company Profile / Pitch Deck Design", "T-shirt / Cap / Mug Merch Mockups", "Full Vector Source Code Rights")
                )
            )
        ),
        ServiceItem(
            id = "srv_poster_banner",
            serviceId = "srv_poster_banner",
            title = "Poster and Banner Design",
            slug = "poster-and-banner-design",
            category = "Graphic Design",
            description = "Large-format outdoor billboard banners, roll-up standees, exhibition backdrops, and promotional event posters.",
            startingPrice = 4000L,
            deliveryTime = "1-2 Days",
            iconName = "crop_original",
            isFeatured = false,
            isActive = true,
            packages = mapOf(
                "Basic" to ServicePackage(
                    name = "Basic",
                    price = 4000L,
                    description = "Standard promotional poster (A4/A3) or digital banner",
                    deliveryTime = "1-2 Days",
                    revisions = "3 Revisions",
                    features = listOf("1 Custom Poster Design", "Print Ready PDF (300 DPI)", "High-Res Digital Version", "3 Revisions")
                ),
                "Standard" to ServicePackage(
                    name = "Standard",
                    price = 8000L,
                    description = "Roll-up standee banner or storefront flex banner with source files",
                    deliveryTime = "2 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("Standee / Flex Banner (Up to 10ft)", "Vector Source File Included", "Commercial Print Specifications", "3D Display Mockup")
                ),
                "Premium" to ServicePackage(
                    name = "Premium",
                    price = 16000L,
                    description = "Event exhibition booth kit (Backdrop, roll-ups, flyers and counter wrap design)",
                    deliveryTime = "4 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("Complete Expo / Booth Suite", "Large Backdrop + 2 Roll-up Banners", "Counter Wrap & Handout Flyer", "Printer-Direct Preflight Checked")
                )
            )
        ),
        ServiceItem(
            id = "srv_custom_digital",
            serviceId = "srv_custom_digital",
            title = "Custom Digital Services",
            slug = "custom-digital-services",
            category = "Custom Solutions",
            description = "Tailored creative projects: Custom vector illustrations, app UI concepts, presentation pitch decks, and digital consultancies.",
            startingPrice = 7500L,
            deliveryTime = "Custom",
            iconName = "auto_awesome",
            isFeatured = true,
            isActive = true,
            packages = mapOf(
                "Basic" to ServicePackage(
                    name = "Basic",
                    price = 7500L,
                    description = "Single custom creative task or 5-slide pitch deck refinement",
                    deliveryTime = "2 Days",
                    revisions = "3 Revisions",
                    features = listOf("1 Specific Custom Requirement", "High Resolution Output", "Direct WhatsApp Coordination", "3 Revisions")
                ),
                "Standard" to ServicePackage(
                    name = "Standard",
                    price = 15000L,
                    description = "Multi-asset custom creative package or 15-slide investor pitch deck",
                    deliveryTime = "4 Days",
                    revisions = "5 Revisions",
                    features = listOf("15-Slide Presentation Deck", "Custom Iconography & Data Charts", "Editable PowerPoint & Keynote", "Commercial Rights")
                ),
                "Premium" to ServicePackage(
                    name = "Premium",
                    price = 35000L,
                    description = "Full custom creative campaign or digital product UI/UX prototype",
                    deliveryTime = "7 Days",
                    revisions = "Unlimited Revisions",
                    features = listOf("End-to-End Creative Direction", "Figma / Vector Master Files", "Clickable Interactive Prototype", "Dedicated WhatsApp Priority Hotline")
                )
            )
        )
    )

    val defaultPortfolio = listOf(
        PortfolioProject(
            id = "port_1",
            projectId = "port_1",
            title = "Apex Logistics Brand Identity",
            description = "Complete luxury brand identity and fleet truck wrap for an international freight and logistics company.",
            category = "Branding",
            clientName = "Apex Logistics",
            completedYear = "2026",
            tag = "Brand Identity",
            isFeatured = true,
            published = true,
            hasBeforeAfter = true,
            beforeText = "Cluttered outdated 2012 clip-art logo with illegible typography.",
            afterText = "Sleek geometric monogram in royal navy with aerodynamic speed cues."
        ),
        PortfolioProject(
            id = "port_2",
            projectId = "port_2",
            title = "UrbanFit Gym Promotional Video",
            description = "High-energy 45-second commercial with fast cuts, kinetic typography, and pulse-pounding sound design.",
            category = "Video & Animation",
            clientName = "UrbanFit Studios",
            completedYear = "2026",
            tag = "Video Production",
            isFeatured = true,
            published = true,
            hasBeforeAfter = true,
            beforeText = "Raw 1080p phone footage with camera shake and background noise.",
            afterText = "Color-graded cinematic 4K commercial with dynamic beat-synced transitions."
        ),
        PortfolioProject(
            id = "port_3",
            projectId = "port_3",
            title = "Zaytoon Gourmet Restaurant Website",
            description = "High-converting online menu booking and reservation website with WhatsApp table orders.",
            category = "Web Development",
            clientName = "Zaytoon Gourmet",
            completedYear = "2026",
            tag = "Website",
            isFeatured = true,
            published = true,
            hasBeforeAfter = false
        ),
        PortfolioProject(
            id = "port_4",
            projectId = "port_4",
            title = "TechSphere 100K YouTube Thumbnails",
            description = "Series of 15 viral YouTube thumbnails resulting in a +42% CTR boost for a leading tech review channel.",
            category = "Social Media",
            clientName = "TechSphere Channel",
            completedYear = "2026",
            tag = "Thumbnails",
            isFeatured = true,
            published = true,
            hasBeforeAfter = true,
            beforeText = "Generic video screenshots with low contrast standard subtitles.",
            afterText = "High-contrast 3D glowing graphics with expressive facial lighting."
        ),
        PortfolioProject(
            id = "port_5",
            projectId = "port_5",
            title = "Nova Coffee Minimalist Packaging",
            description = "Eco-friendly matte black coffee pouch and cup packaging design with gold metallic foil stamps.",
            category = "Graphic Design",
            clientName = "Nova Coffee Roasters",
            completedYear = "2026",
            tag = "Packaging",
            isFeatured = true,
            published = true,
            hasBeforeAfter = false
        ),
        PortfolioProject(
            id = "port_6",
            projectId = "port_6",
            title = "Nexus Cloud SaaS Marketing Campaign",
            description = "30-post LinkedIn and Instagram social campaign featuring infographics and carousel teardowns.",
            category = "Social Media",
            clientName = "Nexus Cloud",
            completedYear = "2026",
            tag = "Social Campaign",
            isFeatured = true,
            published = true,
            hasBeforeAfter = false
        )
    )

    val defaultTestimonials = listOf(
        TestimonialItem(
            id = "test_1",
            clientName = "Kamran Siddiqui",
            roleOrCompany = "Founder, Apex Logistics",
            reviewText = "Hasnat designed our full corporate identity and brand guidelines. The turnaround was super quick, and his communication on WhatsApp was exceptional. Our clients constantly compliment our new logo!",
            rating = 5,
            serviceName = "Brand Identity"
        ),
        TestimonialItem(
            id = "test_2",
            clientName = "Sarah Ahmed",
            roleOrCompany = "Creative Lead, UrbanFit",
            reviewText = "The promotional video Hasnat edited surpassed our expectations! The kinetic captions and sound effects gave it a true Hollywood promo feel. Will definitely book more projects.",
            rating = 5,
            serviceName = "Video Editing"
        ),
        TestimonialItem(
            id = "test_3",
            clientName = "Daniyal Malik",
            roleOrCompany = "YouTube Creator (180K Subs)",
            reviewText = "My CTR went from 4.1% to nearly 9% after Hasnat took over my thumbnails. He understands YouTube psychology and delivers in under 24 hours every time.",
            rating = 5,
            serviceName = "YouTube Thumbnails"
        ),
        TestimonialItem(
            id = "test_4",
            clientName = "Dr. Bilal Tariq",
            roleOrCompany = "Director, Tariq Medical",
            reviewText = "Professional, patient with revisions, and completely delivered on his promises. His quotation system and instant WhatsApp support made the entire booking effortless.",
            rating = 5,
            serviceName = "Website Development"
        )
    )
}
