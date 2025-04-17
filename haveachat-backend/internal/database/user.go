package database

import (
	"database/sql"
	"haveachat-backend/internal/models"
	"haveachat-backend/internal/utils"
	"log"
	"time"
)

// UserRepository handles user-related database operations
type UserRepository struct {
	db *sql.DB
}

// NewUserRepository returns a new UserRepository instance
func NewUserRepository(db *sql.DB) *UserRepository {
	return &UserRepository{db: db}
}

// CreateUser inserts a new user into the database
func (repo *UserRepository) CreateUser(user *models.User) error {
	query := `INSERT INTO users (name, email, created_at, last_login) VALUES (?, ?, ?, ?)`
	result, err := repo.db.Exec(query, user.Name, user.Email, time.Now().UTC(), time.Now().UTC())
	if err != nil {
		log.Println("Error creating user:", err)
		return err
	}

	id, err := result.LastInsertId()
	if err != nil {
		log.Println("Error retrieving user last insert ID:", err)
		return err
	}

	user.ID = int(id)
	log.Println("User created successfully:", user.Email)
	return nil
}

// UpdateLastLogin updates the last_login timestamp for an existing user
func (repo *UserRepository) UpdateLastLogin(email string) error {
	query := `UPDATE users SET last_login = ? WHERE email = ?`
	_, err := repo.db.Exec(query, time.Now().UTC(), email)
	if err != nil {
		log.Println("Error updating last_login for user:", email, err)
		return err
	}
	log.Println("Updated last_login for user:", email)
	return nil
}

// SaveOrUpdateUser checks if a user exists and either creates a new user or updates the last_login timestamp
func (repo *UserRepository) SaveOrUpdateUser(user *models.User) error {
	existingUser, err := repo.GetUserByEmail(user.Email)
	if err != nil {
		log.Println("Error fetching user by email:", err)
		return err
	}

	if existingUser != nil {
		// User exists, update the last_login timestamp
		return repo.UpdateLastLogin(user.Email)
	} else {
		// User does not exist, create a new user
		return repo.CreateUser(user)
	}
}

// GetUserByID retrieves a user by their ID from the database
func (repo *UserRepository) GetUserByID(userID int) (*models.User, error) {
	query := "SELECT id, name, email, created_at, last_login FROM users WHERE id = ?"
	row := repo.db.QueryRow(query, userID)

	// Variables to hold the raw column values
	var id int
	var name, email string
	var createdAt, lastLogin []byte // Use []byte for raw data

	// Attempt to scan into raw variables
	err := row.Scan(&id, &name, &email, &createdAt, &lastLogin)
	if err != nil {
		if err == sql.ErrNoRows {
			log.Println("No user found for ID:", userID)
			return nil, nil
		}
		log.Println("Error scanning user data:", err)
		return nil, err
	}

	// Convert byte slices to strings and then parse to time.Time
	user := &models.User{
		ID:    id,
		Name:  name,
		Email: email,
	}

	// Convert createdAt and lastLogin from []byte to time.Time
	user.CreatedAt, err = utils.ParseTimestamp(createdAt)
	if err != nil {
		log.Println("Error parsing created_at:", err)
	}

	user.LastLogin, err = utils.ParseTimestamp(lastLogin)
	if err != nil {
		log.Println("Error parsing last_login:", err)
	}

	// Successfully found the user
	return user, nil
}

func (repo *UserRepository) GetUserByEmail(userEmail string) (*models.User, error) {
	query := "SELECT id, name, email, created_at, last_login FROM users WHERE email = ?"
	row := repo.db.QueryRow(query, userEmail)

	// Variables to hold the raw column values
	var id int
	var name, email string
	var createdAt, lastLogin []byte // Use []byte for raw data

	// Attempt to scan into raw variables
	err := row.Scan(&id, &name, &email, &createdAt, &lastLogin)
	if err != nil {
		if err == sql.ErrNoRows {
			log.Println("No user found for email:", userEmail)
			return nil, nil
		}
		log.Println("Error scanning user data:", err)
		return nil, err
	}

	// Convert byte slices to strings and then parse to time.Time
	user := &models.User{
		ID:    id,
		Name:  name,
		Email: email,
	}

	// Convert createdAt and lastLogin from []byte to time.Time
	user.CreatedAt, err = utils.ParseTimestamp(createdAt)
	if err != nil {
		log.Println("Error parsing created_at:", err)
	}

	user.LastLogin, err = utils.ParseTimestamp(lastLogin)
	if err != nil {
		log.Println("Error parsing last_login:", err)
	}

	// Successfully found the user
	return user, nil
}
