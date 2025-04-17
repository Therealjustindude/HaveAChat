package database

import (
	"haveachat-backend/internal/models"
	"log"
	"testing"
)

func TestCreateChannel(t *testing.T) {
	srv := New()

	repo := srv.ChannelRepository()

	if repo == nil {
		t.Fatalf("expected channel repository to be returned")
	}

	channel := &models.Channel{
		Name:        "Test Channel",
		Description: "A channel for test db",
		IsPrivate:   true,
	}

	err := repo.CreateChannel(channel)
	if err != nil {
		t.Fatalf("failed to create channel: %v", err)
	}

	log.Println("Channel ID in test:", channel.ID)

	// Verify channel is saved
	savedChannel, err := repo.GetChannelByID(channel.ID)
	if err != nil {
		t.Fatalf("failed to retrieve channel: %v", err)
	}
	if savedChannel == nil {
		t.Fatal("channel not found after creation")
	}
	if savedChannel.Name != channel.Name || savedChannel.Description != channel.Description {
		t.Fatal("retrieved channel does not match created channel")
	}
}

func TestGetChannelByID(t *testing.T) {
	srv := New()

	repo := srv.ChannelRepository()

	if repo == nil {
		t.Fatalf("expected channel repository to be returned")
	}

	// Insert a channel directly into the database for testing
	channel := &models.Channel{
		Name:        "Test Channel 2",
		Description: "A second channel for test db",
		IsPrivate:   true,
	}

	err := repo.CreateChannel(channel)
	if err != nil {
		t.Fatalf("failed to create channel: %v", err)
	}

	foundChannel, err := repo.GetChannelByID(channel.ID)
	if err != nil {
		t.Fatalf("failed to find channel by ID: %v", err)
	}
	if foundChannel == nil {
		t.Fatal("channel not found")
	}
	if foundChannel.Name != channel.Name || foundChannel.Description != channel.Description {
		t.Fatal("retrieved channel does not match expected values")
	}
}
