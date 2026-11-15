# ballot-box

Spring Boot4.0.3/Maven, Java17–25. `mvn -B test`, `mvn -B package` (includes tests),
`java -jar target/ballot-box-0.1.0.jar`. PORT defaults to8305, listener0.0.0.0.
POST /polls {id,question,options}; POST /polls/:id/votes {voter,option}; GET /polls/:id/results. Unique voter per poll, option FK, zero counts included. Synthetic voter markers only.
Requires DATABASE_URL as a postgres/postgresql URI with approved user/password, database and optional sslmode. Run `java -jar target/ballot-box-0.1.0.jar migrate` for source Flyway migrations. Native migrate exits; the web path never auto-migrates. No CREATE DATABASE/clean/reset.
Liveness /health/live. Readiness queries the source-owned tables (/ready for equipment, /health/ready otherwise).
Native tests exercise domain rules and MockMvc routing using a fake repository, not real PostgreSQL.
AI-assisted researcher-created source, common scaffold disclosed separately. No deployment files or runtime credential values.
