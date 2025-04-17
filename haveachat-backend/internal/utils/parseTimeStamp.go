package utils

import (
	"fmt"
	"time"
)

// Helper function to parse timestamp from []byte
func ParseTimestamp(data []byte) (time.Time, error) {
	// Convert byte slice to string
	str := string(data)
	// Parse the string to time.Time
	parsedTime, err := time.Parse("2006-01-02 15:04:05", str)
	if err != nil {
		return time.Time{}, fmt.Errorf("failed to parse time: %v", err)
	}
	return parsedTime, nil
}
