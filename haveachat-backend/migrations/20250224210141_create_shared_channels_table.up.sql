CREATE TABLE shared_channels (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  channel_id INT,
  workspace_id INT,
  target_workspace_id INT,
  origin_workspace_id INT,
	FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
	FOREIGN KEY (workspace_id) REFERENCES workspaces(id) ON DELETE CASCADE,
	FOREIGN KEY (target_workspace_id) REFERENCES workspaces(id) ON DELETE CASCADE,
  FOREIGN KEY (origin_workspace_id) REFERENCES workspaces(id) ON DELETE CASCADE
);
