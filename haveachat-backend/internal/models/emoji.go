package models

// Emoji struct to represent emojis in the database
type Emoji struct {
	ID           int    `json:"id"`
	ChatId       int    `json:"chat_id"`
	UserId       int    `json:"user_id"`
	EmojiUniCode string `json:"emoji_uni_code"`
}
