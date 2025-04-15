package oauth

import (
	"context"
	"crypto/rand"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"haveachat-backend/internal/database"
	"haveachat-backend/internal/models"
	"io"
	"log"
	"net/http"
	"os"

	"github.com/gin-gonic/gin"
	_ "github.com/joho/godotenv/autoload"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
)

// Scopes: OAuth 2.0 scopes provide a way to limit he amount of access that is granted to an access token.
var googleOauthConfig = &oauth2.Config{
	RedirectURL:  "http://localhost:8080/auth/google/callback",
	ClientID:     os.Getenv("GOOGLE_OAUTH_CLIENT_ID"),
	ClientSecret: os.Getenv("GOOGLE_OAUTH_CLIENT_SECRET"),
	Scopes:       []string{"https://www.googleapis.com/auth/userinfo.email"},
	Endpoint:     google.Endpoint,
}

const oauthGoogleUrlAPI = "https://www.googleapis.com/oauth2/v2/userinfo?access_token="

func OauthGoogleLogin(c *gin.Context) {
	// Create oauthState cookie
	oauthState := generateStateOauthCookie(c)
	/*
		AuthCodeURL receive state that is a token to protect the user from CSRF attacks. You must always provide a non-empty string and
		validate that it matches the the state query parameter on your redirect callback.
	*/
	u := googleOauthConfig.AuthCodeURL(oauthState)
	c.Redirect(http.StatusTemporaryRedirect, u)
}

func OauthGoogleCallback(db database.Service) gin.HandlerFunc {
	return func(c *gin.Context) {
		// Retrieve oauthState from cookie
		oauthState, err := c.Request.Cookie("oauthstate")
		if err != nil || c.Query("state") != oauthState.Value {
			log.Println("Invalid OAuth state")
			c.Redirect(http.StatusTemporaryRedirect, "/")
			return
		}

		// Exchange code for user data
		data, err := getUserDataFromGoogle(c.Query("code"))
		if err != nil {
			log.Println("Error retrieving user data:", err)
			c.Redirect(http.StatusTemporaryRedirect, "/")
			return
		}

		// Access the UserRepository through the Service interface
		userRepo := db.UserRepository()

		// Save user to DB
		user := &models.User{
			Name:  data.Name,
			Email: data.Email,
		}

		if err := userRepo.SaveOrUpdateUser(user); err != nil {
			log.Println("Error saving/updating user:", err)
			c.Redirect(http.StatusInternalServerError, "/")
			return
		}

		// Respond with user information
		c.JSON(http.StatusOK, gin.H{"user": data})
	}
}

func generateStateOauthCookie(c *gin.Context) string {
	expiration := 365 * 24 * 60 * 60

	b := make([]byte, 16)
	rand.Read(b)
	state := base64.URLEncoding.EncodeToString(b)

	c.SetCookie("oauthstate", state, expiration, "/", "", false, true)
	return state
}

type GoogleUser struct {
	ID            string `json:"id"`
	Email         string `json:"email"`
	VerifiedEmail bool   `json:"verified_email"`
	Name          string `json:"name"`
	GivenName     string `json:"given_name"`
	FamilyName    string `json:"family_name"`
	Picture       string `json:"picture"`
	Locale        string `json:"locale"`
}

func getUserDataFromGoogle(code string) (*GoogleUser, error) {
	// Use code to get token and get user info from Google.
	token, err := googleOauthConfig.Exchange(context.Background(), code)
	if err != nil {
		return nil, fmt.Errorf("code exchange wrong: %s", err.Error())
	}
	response, err := http.Get(oauthGoogleUrlAPI + token.AccessToken)
	if err != nil {
		return nil, fmt.Errorf("failed getting user info: %s", err.Error())
	}
	defer response.Body.Close()
	contents, err := io.ReadAll(response.Body)
	if err != nil {
		return nil, fmt.Errorf("failed read response: %s", err.Error())
	}

	// Log the raw JSON response for debugging
	log.Printf("Raw JSON response from Google: %s", contents)

	// Unmarshal the JSON response into the User struct
	var user GoogleUser
	err = json.Unmarshal(contents, &user)
	if err != nil {
		return nil, fmt.Errorf("failed unmarshalling user data: %s", err.Error())
	}

	// Return the parsed user data
	return &user, nil
}
