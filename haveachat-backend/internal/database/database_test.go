package database

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"testing"
	"time"

	"github.com/testcontainers/testcontainers-go"
	"github.com/testcontainers/testcontainers-go/modules/mysql"
	"github.com/testcontainers/testcontainers-go/wait"
)

func mustStartMySQLContainer() (func(context.Context, ...testcontainers.TerminateOption) error, error) {
	var (
		dbName = "database"
		dbPwd  = "password"
		dbUser = "user"
	)

	dbContainer, err := mysql.Run(context.Background(),
		"mysql:8.0.36",
		mysql.WithDatabase(dbName),
		mysql.WithUsername(dbUser),
		mysql.WithPassword(dbPwd),
		testcontainers.WithWaitStrategy(wait.ForLog("port: 3306  MySQL Community Server - GPL").WithStartupTimeout(30*time.Second)),
	)
	if err != nil {
		return nil, err
	}

	dbname = dbName
	password = dbPwd
	username = dbUser

	dbHost, err := dbContainer.Host(context.Background())
	if err != nil {
		return dbContainer.Terminate, err
	}

	dbPort, err := dbContainer.MappedPort(context.Background(), "3306/tcp")
	if err != nil {
		return dbContainer.Terminate, err
	}

	host = dbHost
	port = dbPort.Port()

	// Initialize the DB connection
	db, err := sql.Open("mysql", fmt.Sprintf("%s:%s@tcp(%s:%s)/%s", username, password, host, port, dbname))
	if err != nil {
		return dbContainer.Terminate, err
	}

	// table creation queries
	createUserTableQuery := `
		CREATE TABLE users (
			id INT AUTO_INCREMENT PRIMARY KEY,
			name VARCHAR(50) NOT NULL,
			email VARCHAR(255) UNIQUE,
			created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
			last_login TIMESTAMP DEFAULT NULL
		);
	`
	createMemberTableQuery := `
		CREATE TABLE members (
			id INT AUTO_INCREMENT PRIMARY KEY,
			channel_id INT,
			user_id INT,
			created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
			FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
			FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
		);
	`
	createChannelTableQuery := `
		CREATE TABLE channels (
			id INT AUTO_INCREMENT PRIMARY KEY,
			name VARCHAR(255) NOT NULL,
			description TEXT,
			created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
			is_private BOOLEAN DEFAULT FALSE
		);
	`

	// Execute the user table creation query
	_, err = db.Exec(createUserTableQuery)
	if err != nil {
		return dbContainer.Terminate, fmt.Errorf("could not create users table: %v", err)
	}

	// Execute the channel table creation query
	_, err = db.Exec(createChannelTableQuery)
	if err != nil {
		return dbContainer.Terminate, fmt.Errorf("could not create members table: %v", err)
	}

	// Execute the member table creation query
	_, err = db.Exec(createMemberTableQuery)
	if err != nil {
		return dbContainer.Terminate, fmt.Errorf("could not create members table: %v", err)
	}

	return dbContainer.Terminate, err
}

func TestMain(m *testing.M) {
	teardown, err := mustStartMySQLContainer()
	if err != nil {
		log.Fatalf("could not start mysql container: %v", err)
	}

	m.Run()

	if teardown != nil && teardown(context.Background()) != nil {
		log.Fatalf("could not teardown mysql container: %v", err)
	}
}

func TestNew(t *testing.T) {
	srv := New()
	if srv == nil {
		t.Fatal("New() returned nil")
	}
}

func TestHealth(t *testing.T) {
	srv := New()

	stats := srv.Health()

	if stats["status"] != "up" {
		t.Fatalf("expected status to be up, got %s", stats["status"])
	}

	if _, ok := stats["error"]; ok {
		t.Fatalf("expected error not to be present")
	}

	if stats["message"] != "It's healthy" {
		t.Fatalf("expected message to be 'It's healthy', got %s", stats["message"])
	}
}

// func TestClose(t *testing.T) {
// 	srv := New()

// 	if srv.Close() != nil {
// 		t.Fatalf("expected Close() to return nil")
// 	}
// }
