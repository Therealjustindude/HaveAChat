package models

import (
	"time"
)

// Member struct to represent a members in the database
type Member struct {
	ID        int       `json:"id"`
	ChannelId int       `json:"channel_id"`
	UserId    int       `json:"user_id"`
	CreatedAt time.Time `json:"created_at"`
}
