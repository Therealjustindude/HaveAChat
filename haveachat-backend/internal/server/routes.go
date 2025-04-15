package server

import (
	"log"
	"net/http"
	"time"

	"haveachat-backend/internal/oauth"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
)

func (s *Server) RegisterRoutes() *gin.Engine {
	router := gin.New()

	// Apply CORS middleware with custom configuration
	router.Use(cors.New(cors.Config{
		AllowOrigins:     []string{"http://localhost:3000"}, // Replace with your frontend's origin
		AllowMethods:     []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowHeaders:     []string{"Origin", "Content-Type", "Authorization"},
		ExposeHeaders:    []string{"Content-Length"},
		AllowCredentials: true,
		MaxAge:           12 * time.Hour,
	}))

	router.Use(s.loggingMiddleware())

	// Define routes
	router.GET("/health", s.healthHandler)

	router.GET("/auth/google/login", oauth.OauthGoogleLogin)

	router.GET("/auth/google/callback", oauth.OauthGoogleCallback(s.db))

	return router
}

func (s *Server) loggingMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		log.Printf("Received request for %s", c.Request.URL.Path)
		c.Next()
	}
}

func (s *Server) HelloWorldHandler(c *gin.Context) {
	c.JSON(http.StatusOK, gin.H{"message": "Hello World"})
}

func (s *Server) healthHandler(c *gin.Context) {
	c.JSON(http.StatusOK, s.db.Health())
}
