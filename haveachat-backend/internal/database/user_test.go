package database

import (
	"haveachat-backend/internal/models"
	"testing"
	"time"
)

func TestCreateUser(t *testing.T) {
	srv := New()

	repo := srv.UserRepository()

	if repo == nil {
		t.Fatalf("expected user repository to be returned")
	}

	user := &models.User{
		Name:  "John Doe",
		Email: "john.doe@example.com",
	}

	// Ensure user is created successfully
	err := repo.CreateUser(user)
	if err != nil {
		t.Fatalf("failed to create user: %v", err)
	}

	// Verify user is saved in DB (fetch and check)
	fetchedUser, err := repo.GetUserByEmail(user.Email)
	if err != nil {
		t.Fatalf("failed to fetch user by email: %v", err)
	}
	if fetchedUser.Email != user.Email {
		t.Fatalf("expected user email to be %s, got %s", user.Email, fetchedUser.Email)
	}

	// Test case 2: Handle user creation failure (e.g., duplicate email)
	duplicateUser := &models.User{
		Name:  "Jane Doe",
		Email: user.Email, // Same email as the previous user
	}

	err = repo.CreateUser(duplicateUser)
	if err == nil {
		t.Errorf("expected error when creating user with duplicate email")
	} else {
		t.Logf("received expected error when attempting to create duplicate user: %v", err)
	}
}

func TestGetUserByID(t *testing.T) {
	srv := New()

	repo := srv.UserRepository()

	if repo == nil {
		t.Fatalf("expected user repository to be returned")
	}

	user := &models.User{
		Email: "john.doe@example.com",
	}

	foundUser, err := repo.GetUserByID(1)
	if err != nil {
		t.Fatalf("failed to find user ID: %v", err)
	}

	if user.Email != foundUser.Email {
		t.Fatalf("user found by ID does not match expected user: %v", err)
	}
}
func TestGetUserByEmail(t *testing.T) {
	srv := New()

	repo := srv.UserRepository()

	if repo == nil {
		t.Fatalf("expected user repository to be returned")
	}

	user := &models.User{
		Email: "john.doe@example.com",
	}

	foundUser, err := repo.GetUserByEmail("john.doe@example.com")
	if err != nil {
		t.Fatalf("failed to find user ID: %v", err)
	}

	if user.Email != foundUser.Email {
		t.Fatalf("user found by ID does not match expected user: %v", err)
	}
}

func TestSaveOrUpdateUser(t *testing.T) {
	srv := New()

	repo := srv.UserRepository()

	if repo == nil {
		t.Fatalf("expected user repository to be returned")
	}

	user := &models.User{
		Name:  "Jane Doe",
		Email: "jane.doe@example.com",
	}

	// First, create the user
	err := repo.CreateUser(user)
	if err != nil {
		t.Fatalf("failed to create user: %v", err)
	}

	// Ensure user is fetched and last_login is set
	fetchedUser, err := repo.GetUserByEmail(user.Email)
	if err != nil {
		t.Fatalf("failed to fetch user by email: %v", err)
	}
	if fetchedUser.LastLogin.IsZero() {
		t.Fatalf("expected last_login to be set, but it was zero")
	}

	time.Sleep(1 * time.Minute) // wait on minute so time changes in db

	// Now update the user (this should update last_login)
	err = repo.SaveOrUpdateUser(user)
	if err != nil {
		t.Fatalf("failed to save or update user: %v", err)
	}

	// Fetch the user again after the update
	updatedUser, err := repo.GetUserByEmail(user.Email)
	if err != nil {
		t.Fatalf("failed to fetch user by email: %v", err)
	}

	// Ensure that the last_login time has been updated (it should be different from before)
	if updatedUser.LastLogin.Equal(fetchedUser.LastLogin) {
		t.Fatalf("expected last_login to be updated, but it wasn't")
	}
}
