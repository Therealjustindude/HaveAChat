import { createContext, useContext, useEffect, useState } from "react"

type Theme = "dark" | "light" | "system"

type ThemeProviderProps = {
  children: React.ReactNode
  defaultTheme?: Theme
  storageKey?: string
}

type ThemeContextType = {
  theme: Theme;
  setTheme: (t: Theme) => void;
};


const ThemeContext = createContext<ThemeContextType>({
  theme: "light",
  setTheme: () => {},
});

export const ThemeProvider = ({
  children,
  defaultTheme = "light",
  storageKey = "haveachat-theme",
}: ThemeProviderProps) => {
  const [theme, setTheme] = useState<Theme>(defaultTheme);

  useEffect(() => {
    if (typeof window !== "undefined") {
      const storedTheme = localStorage.getItem(storageKey) as Theme | null;
      if (storedTheme && ["light", "dark", "system"].includes(storedTheme)) {
        setTheme(storedTheme);
      }
    }
  }, [storageKey]);

  useEffect(() => {
    if (typeof window !== "undefined") {
      localStorage.setItem(storageKey, theme);
    }
  }, [theme, storageKey]);

  return (
    <ThemeContext.Provider value={{ theme, setTheme }}>
      {children}
    </ThemeContext.Provider>
  );
};

export const useTheme = () => {
  const context = useContext(ThemeContext)

  if (context === undefined)
    throw new Error("useTheme must be used within a ThemeProvider")

  return context
}