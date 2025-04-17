package database

import (
	"database/sql"
	"haveachat-backend/internal/models"
	"haveachat-backend/internal/utils"
	"log"
	"time"
)

// ChannelRepository handles channel-related database operations
type ChannelRepository struct {
	db *sql.DB
}

// NewChannelRepository returns a new ChannelRepository instance
func NewChannelRepository(db *sql.DB) *ChannelRepository {
	return &ChannelRepository{db: db}
}

// CreateChannel inserts a new channel into the database
func (repo *ChannelRepository) CreateChannel(channel *models.Channel) error {
	query := `INSERT INTO channels (name, description, created_at, is_private) VALUES (?, ?, ?, ?)`
	result, err := repo.db.Exec(query, channel.Name, channel.Description, time.Now().UTC(), channel.IsPrivate)
	if err != nil {
		log.Println("Error creating channel:", err)
		return err
	}

	id, err := result.LastInsertId()
	if err != nil {
		log.Println("Error retrieving channel last insert ID:", err)
		return err
	}

	channel.ID = int(id)
	log.Println("Channel created successfully:", channel.Name)
	log.Println("Channel created ID:", channel.ID)
	return nil
}

// GetChannelByID retrieves a channel by their ID from the database
func (repo *ChannelRepository) GetChannelByID(channelID int) (*models.Channel, error) {
	query := "SELECT id, name, description, created_at, is_private FROM channels WHERE id = ?"
	row := repo.db.QueryRow(query, channelID)

	// Variables to hold the raw column values
	var id int
	var name, description string
	var is_private bool
	var createdAt []byte // Use []byte for raw data

	// Attempt to scan into raw variables
	err := row.Scan(&id, &name, &description, &createdAt, &is_private)
	if err != nil {
		if err == sql.ErrNoRows {
			log.Println("No channel found for ID:", channelID)
			return nil, nil
		}
		log.Println("Error scanning channel data:", err)
		return nil, err
	}

	// Convert byte slices to strings and then parse to time.Time
	channel := &models.Channel{
		ID:          id,
		Name:        name,
		Description: description,
		IsPrivate:   is_private,
	}

	// Convert createdAt and createdAt from []byte to time.Time
	channel.CreatedAt, err = utils.ParseTimestamp(createdAt)
	if err != nil {
		log.Println("Error parsing created_at:", err)
	}

	// Successfully found the channel
	return channel, nil
}
