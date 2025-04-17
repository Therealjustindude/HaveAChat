package models

import (
	"time"
)

// Channel struct to represent channels in the database
type Channel struct {
	ID          int       `json:"id"`
	Name        string    `json:"name"`
	Description string    `json:"description"`
	CreatedAt   time.Time `json:"created_at"`
	IsPrivate   bool      `json:"is_private"`
}
