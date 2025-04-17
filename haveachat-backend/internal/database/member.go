package database

import (
	"database/sql"
	"fmt"
	"haveachat-backend/internal/models"
	"haveachat-backend/internal/utils"
	"log"
	"time"
)

// MemberRepository handles member-related database operations
type MemberRepository struct {
	db *sql.DB
}

// NewMemberRepository returns a new MemberRepository instance
func NewMemberRepository(db *sql.DB) *MemberRepository {
	return &MemberRepository{db: db}
}

// CreateMember inserts a new member into the database
func (repo *MemberRepository) CreateMember(member *models.Member) error {
	query := `INSERT INTO members (channel_id, user_id, created_at) VALUES (?, ?, ?)`
	result, err := repo.db.Exec(query, member.ChannelId, member.UserId, time.Now().UTC())
	if err != nil {
		log.Println("Error creating member:", err)
		return err
	}

	id, err := result.LastInsertId()
	if err != nil {
		log.Println("Error retrieving member last insert ID:", err)
		return err
	}

	member.ID = int(id)

	log.Println("Member created successfully:", member.UserId)
	return nil
}

// GetMemberByID retrieves a member by their ID from the database
func (repo *MemberRepository) GetMemberByID(memberID int) (*models.Member, error) {
	query := "SELECT id, channel_id, user_id, created_at FROM members WHERE id = ?"
	row := repo.db.QueryRow(query, memberID)

	// Variables to hold the raw column values
	var id int
	var channel_id, user_id int
	var createdAt []byte // Use []byte for raw data

	// Attempt to scan into raw variables
	err := row.Scan(&id, &channel_id, &user_id, &createdAt)
	if err != nil {
		if err == sql.ErrNoRows {
			log.Println("No member found for ID:", memberID)
			return nil, nil
		}
		log.Println("Error scanning member data:", err)
		return nil, err
	}

	// Convert byte slices to strings and then parse to time.Time
	member := &models.Member{
		ID:        id,
		ChannelId: channel_id,
		UserId:    user_id,
	}

	// Convert createdAt and CreatedAt from []byte to time.Time
	member.CreatedAt, err = utils.ParseTimestamp(createdAt)
	if err != nil {
		log.Println("Error parsing created_at:", err)
	}

	// Successfully found the member
	return member, nil
}

// GetMemberByUserID retrieves a member by their user ID from the database
func (repo *MemberRepository) GetMemberByUserID(userID int) (*models.Member, error) {
	query := "SELECT id, channel_id, user_id, created_at FROM members WHERE user_id = ?"
	row := repo.db.QueryRow(query, userID)

	// Variables to hold the raw column values
	var id int
	var channel_id, user_id int
	var createdAt []byte // Use []byte for raw data

	// Attempt to scan into raw variables
	err := row.Scan(&id, &channel_id, &user_id, &createdAt)
	if err != nil {
		if err == sql.ErrNoRows {
			log.Println("No member found for user ID:", userID)
			return nil, nil
		}
		log.Println("Error scanning member data:", err)
		return nil, err
	}

	// Convert byte slices to strings and then parse to time.Time
	member := &models.Member{
		ID:        id,
		ChannelId: channel_id,
		UserId:    user_id,
	}

	// Convert createdAt and lastLogin from []byte to time.Time
	member.CreatedAt, err = utils.ParseTimestamp(createdAt)
	if err != nil {
		log.Println("Error parsing created_at:", err)
	}

	// Successfully found the member
	return member, nil
}

// GetMembersByChannelID retrieves a members by their channel ID from the database
func (repo *MemberRepository) GetMembersByChannelID(channelID int) ([]*models.Member, error) {
	query := "SELECT id, channel_id, user_id, created_at FROM members WHERE channel_id = ?"
	rows, err := repo.db.Query(query, channelID)
	if err != nil {
		return nil, fmt.Errorf("query error: %w", err)
	}
	defer rows.Close()

	var members []*models.Member

	for rows.Next() {
		var id int
		var channel_id, user_id int
		var createdAt []byte // Use []byte for raw data

		err := rows.Scan(&id, &channel_id, &user_id, &createdAt)
		if err != nil {
			if err == sql.ErrNoRows {
				log.Println("No members found for channel ID:", channelID)
				return nil, nil
			}
			log.Println("Error scanning member data:", err)
			return nil, err
		}

		// Convert byte slices to strings and then parse to time.Time
		member := &models.Member{
			ID:        id,
			ChannelId: channel_id,
			UserId:    user_id,
		}
		member.CreatedAt, err = utils.ParseTimestamp(createdAt)
		if err != nil {
			log.Println("Error parsing created_at:", err)
		}

		members = append(members, member)
	}

	if err = rows.Err(); err != nil {
		return nil, fmt.Errorf("rows iteration error: %w", err)
	}

	return members, nil
}
