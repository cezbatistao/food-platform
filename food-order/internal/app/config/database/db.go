package database

import (
    "database/sql"
    "fmt"

    "github.com/golang-migrate/migrate/v4"

    "github.com/cezbatistao/food-platform/food-order/internal/app/config"
    "github.com/golang-migrate/migrate/v4/database/postgres"
    _ "github.com/golang-migrate/migrate/v4/source/file"
)

func Init() (*sql.DB, error) {
    var db *sql.DB
    var err error

    var databaseUrl = config.DatabaseUrl()

    db, err = sql.Open(config.PostgresDriver(), databaseUrl)
    if err != nil {
        panic(err.Error())
    }

    err = migrateDatabase(db)
    if err != nil {
        return nil, err
    }

    return db, nil
}

func migrateDatabase(db *sql.DB) error {
    migrationFolder := fmt.Sprintf("file://%s/db/migration", config.Dirname())

    driver, err := postgres.WithInstance(db, &postgres.Config{})
    m, err := migrate.NewWithDatabaseInstance(migrationFolder, config.DatabaseName(), driver)
    m.Up()
    if err != nil {
        // **I get error here!!**
        return fmt.Errorf("error happened when migration")
    }
    if err := m.Up(); err != nil && err != migrate.ErrNoChange {
        return fmt.Errorf("error when migration up: %v", err)
    }

    return nil
}