package edu.famu.cop3060.yard.config;

import edu.famu.cop3060.yard.entity.Opportunity;

import java.util.List;

/**
 * Canonical seed opportunities (formerly in {@code InMemoryOpportunityStore}).
 */
public final class OpportunitySeedData {

    private OpportunitySeedData() {
    }

    public static List<Opportunity> all() {
        return List.of(
                new Opportunity(
                        "opp-001",
                        "UNCF STEM Scholarship",
                        "Scholarship",
                        "UNCF",
                        "2025-04-15",
                        "Merit-based scholarship for STEM majors at HBCUs.",
                        List.of("STEM", "undergrad", "paid"),
                        "https://uncf.org/programs/uncf-stem-scholarship"
                ),
                new Opportunity(
                        "opp-002",
                        "Google HBCU Career Residency",
                        "Fellowship",
                        "Google",
                        "2025-03-01",
                        "Paid residency program for recent HBCU grads in tech.",
                        List.of("tech", "paid", "summer"),
                        "https://careers.google.com/students/hbcu"
                ),
                new Opportunity(
                        "opp-003",
                        "National Society of Black Engineers Chapter",
                        "Organization",
                        "NSBE",
                        "",
                        "Student chapter for networking and professional development in engineering.",
                        List.of("engineering", "networking", "STEM"),
                        "https://www.nsbe.org"
                ),
                new Opportunity(
                        "opp-004",
                        "Homecoming Step Show Registration",
                        "Event",
                        "Student Government",
                        "2025-10-01",
                        "Register your step team for the annual homecoming step show.",
                        List.of("culture", "performing arts", "campus"),
                        "https://example.edu/homecoming/stepshow"
                ),
                new Opportunity(
                        "opp-005",
                        "Goldman Sachs Summer Analyst Program",
                        "Internship",
                        "Goldman Sachs",
                        "2025-01-15",
                        "Summer analyst internship for juniors and seniors in finance.",
                        List.of("finance", "paid", "junior", "senior"),
                        "https://www.goldmansachs.com/careers/students"
                ),
                new Opportunity(
                        "opp-006",
                        "Thurgood Marshall College Fund Scholarship",
                        "Scholarship",
                        "TMCF",
                        "2025-05-01",
                        "Scholarships for students attending TMCF member schools.",
                        List.of("undergrad", "graduate", "leadership"),
                        "https://www.tmcf.org/scholarships"
                ),
                new Opportunity(
                        "opp-007",
                        "Microsoft LEAP Engineering Acceleration Program",
                        "Fellowship",
                        "Microsoft",
                        "2025-02-28",
                        "Technical fellowship for career switchers and bootcamp grads.",
                        List.of("tech", "paid", "engineering"),
                        "https://careers.microsoft.com/leap"
                ),
                new Opportunity(
                        "opp-008",
                        "Black Engineer of the Year Award Conference",
                        "Event",
                        "BEYA",
                        "2025-01-31",
                        "Annual conference and career fair for STEM professionals and students.",
                        List.of("STEM", "networking", "career fair"),
                        "https://www.beya.org"
                )
        );
    }
}
