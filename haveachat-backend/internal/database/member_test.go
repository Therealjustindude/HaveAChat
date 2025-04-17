package database

import (
	"haveachat-backend/internal/models"
	"testing"
)

func TestCreateMember(t *testing.T) {
	srv := New()

	repo := srv.MemberRepository()

	userRepo := srv.UserRepository()

	if repo == nil {
		t.Fatalf("expected member repository to be returned")
	}

	if userRepo == nil {
		t.Fatalf("expected user repository to be returned")
	}

	user := &models.User{
		Name:  "Shane Doe",
		Email: "Shane.doe@example.com",
	}

	// Ensure user is created successfully
	userErr := userRepo.CreateUser(user)
	if userErr != nil {
		t.Fatalf("failed to create user: %v", userErr)
	}

	member := &models.Member{
		ChannelId: 1,
		UserId:    user.ID,
	}

	err := repo.CreateMember(member)
	if err != nil {
		t.Fatalf("failed to create member: %v", err)
	}

	// Verify member is saved
	savedMember, err := repo.GetMemberByUserID(member.UserId)
	if err != nil {
		t.Fatalf("failed to retrieve member: %v", err)
	}
	if savedMember == nil {
		t.Fatal("member not found after creation")
	}
	if savedMember.ChannelId != member.ChannelId || savedMember.UserId != member.UserId {
		t.Fatal("retrieved member does not match created member")
	}
}

func TestGetMemberByID(t *testing.T) {
	srv := New()

	repo := srv.MemberRepository()

	userRepo := srv.UserRepository()

	if repo == nil {
		t.Fatalf("expected member repository to be returned")
	}

	if userRepo == nil {
		t.Fatalf("expected user repository to be returned")
	}

	user := &models.User{
		Name:  "Shawn Doe",
		Email: "Shawn.doe@example.com",
	}

	// Ensure user is created successfully
	userErr := userRepo.CreateUser(user)
	if userErr != nil {
		t.Fatalf("failed to create user: %v", userErr)
	}

	// Insert a member directly into the database for testing
	member := &models.Member{
		ChannelId: 2,
		UserId:    user.ID,
	}

	err := repo.CreateMember(member)
	if err != nil {
		t.Fatalf("failed to create member: %v", err)
	}

	foundMember, err := repo.GetMemberByID(member.ID)
	if err != nil {
		t.Fatalf("failed to find member by ID: %v", err)
	}
	if foundMember == nil {
		t.Fatal("member not found")
	}
	if foundMember.ChannelId != member.ChannelId || foundMember.UserId != member.UserId {
		t.Fatal("retrieved member does not match expected values")
	}
}
