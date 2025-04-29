import { NextResponse } from "next/server"

// This would connect to your Redis instance in a real application
// For now, we'll simulate the API response

export async function GET(request: Request) {
  try {
    const { searchParams } = new URL(request.url)
    const game = searchParams.get("game")
    const limit = Number.parseInt(searchParams.get("limit") || "10")

    // In a real app, you would:
    // 1. Connect to Redis
    // 2. Query the sorted set for the specified game
    // 3. Return the top N players

    // Sample data for demonstration
    const players = [
      { id: "1", username: "GameMaster", score: 1250, rank: 1 },
      { id: "2", username: "ProGamer99", score: 1100, rank: 2 },
      { id: "3", username: "PixelWarrior", score: 980, rank: 3 },
      { id: "4", username: "NinjaPlayer", score: 920, rank: 4 },
      { id: "5", username: "PlayerOne", score: 850, rank: 5 },
      { id: "6", username: "GamerX", score: 800, rank: 6 },
      { id: "7", username: "VictoryQueen", score: 750, rank: 7 },
      { id: "8", username: "LegendGamer", score: 700, rank: 8 },
      { id: "9", username: "EpicPlayer", score: 650, rank: 9 },
      { id: "10", username: "GameChampion", score: 600, rank: 10 },
    ]

    // Filter by game if specified
    const filteredPlayers = game
      ? players.filter((p) => Math.random() > 0.5) // Simulate game filtering
      : players

    return NextResponse.json({
      players: filteredPlayers.slice(0, limit),
    })
  } catch (error) {
    console.error("Error fetching leaderboard:", error)
    return NextResponse.json({ error: "Failed to fetch leaderboard" }, { status: 500 })
  }
}
