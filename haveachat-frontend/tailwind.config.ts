import type { Config } from 'tailwindcss'

const config: Config = {
  content: ['./index.html', './src/**/*.{ts,tsx,vue}'],
  theme: {
    extend: {
      fontFamily: {
				pacifico: ['var(--font-pacifico)'],
			},
    },
  },
  plugins: [],
}

export default config