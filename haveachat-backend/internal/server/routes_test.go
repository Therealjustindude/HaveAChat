package server

import (
	"io"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/gin-gonic/gin"
)

func TestHandler(t *testing.T) {
	// Initialize a Gin instance
	r := gin.Default()

	// Register the route with Gin
	r.GET("/hello", func(c *gin.Context) {
		s := &Server{}
		s.HelloWorldHandler(c)
	})

	// Create the test server
	server := httptest.NewServer(r)
	defer server.Close()

	// Make the GET request
	resp, err := http.Get(server.URL + "/hello")
	if err != nil {
		t.Fatalf("error making request to server. Err: %v", err)
	}
	defer resp.Body.Close()

	// Assertions
	if resp.StatusCode != http.StatusOK {
		t.Errorf("expected status OK; got %v", resp.Status)
	}

	expected := "{\"message\":\"Hello World\"}"
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		t.Fatalf("error reading response body. Err: %v", err)
	}
	if expected != string(body) {
		t.Errorf("expected response body to be %v; got %v", expected, string(body))
	}
}
