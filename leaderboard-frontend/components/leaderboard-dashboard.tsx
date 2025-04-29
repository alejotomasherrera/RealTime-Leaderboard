"use client"

import { useState, useEffect } from "react"
import { useToast } from "@/hooks/use-toast"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Button } from "@/components/ui/button"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Trophy, Medal, Award, Crown, ArrowUpRight, ArrowDownRight, Minus } from "lucide-react"
import ScoreSubmissionForm from "./score-submission-form"
import UserProfile from "./user-profile"

// Types for our leaderboard data
type Player = {
  id: string
  username: string
  score: number
  rank: number
  avatar?: string
  previousRank?: number
}

export default function LeaderboardDashboard() {
  const [players, setPlayers] = useState<Player[]>([])
  const [isConnected, setIsConnected] = useState(false)
  const [activeTab, setActiveTab] = useState("leaderboard")
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [currentUser, setCurrentUser] = useState<Player | null>(null)
  const { toast } = useToast()

  useEffect(() => {
    // Simulate logged in user for demo purposes
    // In a real app, you would check authentication status
    setIsLoggedIn(true)
    setCurrentUser({
      id: "user123",
      username: "PlayerOne",
      score: 850,
      rank: 5,
      previousRank: 7,
    })

    // Connect to WebSocket
    const socket = new WebSocket("ws://localhost:8080") // Replace with your actual WebSocket URL

    socket.onopen = () => {
      setIsConnected(true)
      toast({
        title: "Connected to server",
        description: "You are now receiving real-time updates",
      })
    }

    socket.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data)
        if (data.type === "leaderboard_update") {
          setPlayers(data.players)

          // Update current user if they're in the leaderboard
          if (isLoggedIn && currentUser) {
            const updatedUser = data.players.find((p: Player) => p.id === currentUser.id)
            if (updatedUser) {
              setCurrentUser(updatedUser)
            }
          }
        }
      } catch (error) {
        console.error("Error parsing WebSocket message:", error)
      }
    }

    socket.onclose = () => {
      setIsConnected(false)
      toast({
        title: "Disconnected from server",
        description: "Connection to the leaderboard server was lost",
        variant: "destructive",
      })
    }

    // For demo purposes, let's populate with sample data
    const samplePlayers: Player[] = [
      {
        id: "1",
        username: "GameMaster",
        score: 1250,
        rank: 1,
        previousRank: 1,
        avatar: "/placeholder.svg?height=40&width=40",
      },
      {
        id: "2",
        username: "ProGamer99",
        score: 1100,
        rank: 2,
        previousRank: 3,
        avatar: "/placeholder.svg?height=40&width=40",
      },
      {
        id: "3",
        username: "PixelWarrior",
        score: 980,
        rank: 3,
        previousRank: 2,
        avatar: "/placeholder.svg?height=40&width=40",
      },
      {
        id: "4",
        username: "NinjaPlayer",
        score: 920,
        rank: 4,
        previousRank: 4,
        avatar: "/placeholder.svg?height=40&width=40",
      },
      {
        id: "user123",
        username: "PlayerOne",
        score: 850,
        rank: 5,
        previousRank: 7,
        avatar: "/placeholder.svg?height=40&width=40",
      },
      {
        id: "6",
        username: "GamerX",
        score: 800,
        rank: 6,
        previousRank: 5,
        avatar: "/placeholder.svg?height=40&width=40",
      },
      {
        id: "7",
        username: "VictoryQueen",
        score: 750,
        rank: 7,
        previousRank: 6,
        avatar: "/placeholder.svg?height=40&width=40",
      },
      {
        id: "8",
        username: "LegendGamer",
        score: 700,
        rank: 8,
        previousRank: 9,
        avatar: "/placeholder.svg?height=40&width=40",
      },
      {
        id: "9",
        username: "EpicPlayer",
        score: 650,
        rank: 9,
        previousRank: 8,
        avatar: "/placeholder.svg?height=40&width=40",
      },
      {
        id: "10",
        username: "GameChampion",
        score: 600,
        rank: 10,
        previousRank: 10,
        avatar: "/placeholder.svg?height=40&width=40",
      },
    ]
    setPlayers(samplePlayers)

    // Cleanup function
    return () => {
      socket.close()
    }
  }, [])

  const getRankChangeIcon = (current: number, previous?: number) => {
    if (!previous || current === previous) {
      return <Minus className="h-4 w-4 text-gray-400" />
    } else if (current < previous) {
      return <ArrowUpRight className="h-4 w-4 text-green-500" />
    } else {
      return <ArrowDownRight className="h-4 w-4 text-red-500" />
    }
  }

  const getRankIcon = (rank: number) => {
    switch (rank) {
      case 1:
        return <Trophy className="h-5 w-5 text-yellow-400" />
      case 2:
        return <Medal className="h-5 w-5 text-gray-300" />
      case 3:
        return <Award className="h-5 w-5 text-amber-600" />
      default:
        return <Crown className="h-5 w-5 text-slate-600" />
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold">Global Rankings</h2>
          <p className="text-slate-300">
            {isConnected ? (
              <span className="flex items-center">
                <span className="h-2 w-2 rounded-full bg-green-500 mr-2"></span>
                Live updates enabled
              </span>
            ) : (
              <span className="flex items-center">
                <span className="h-2 w-2 rounded-full bg-red-500 mr-2"></span>
                Disconnected
              </span>
            )}
          </p>
        </div>
        {isLoggedIn && currentUser && (
          <div className="text-right">
            <p className="text-sm text-slate-300">Your Rank</p>
            <p className="text-2xl font-bold flex items-center justify-end">
              #{currentUser.rank}
              <span className="ml-2 text-sm">{getRankChangeIcon(currentUser.rank, currentUser.previousRank)}</span>
            </p>
          </div>
        )}
      </div>

      <Tabs defaultValue="leaderboard" className="w-full" onValueChange={setActiveTab}>
        <TabsList className="grid w-full grid-cols-3">
          <TabsTrigger value="leaderboard">Leaderboard</TabsTrigger>
          <TabsTrigger value="submit">Submit Score</TabsTrigger>
          <TabsTrigger value="profile">Your Profile</TabsTrigger>
        </TabsList>

        <TabsContent value="leaderboard" className="mt-6">
          <Card className="bg-slate-800 border-slate-700">
            <CardHeader className="pb-2">
              <CardTitle>Top 10 Players</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {players.map((player) => (
                  <div
                    key={player.id}
                    className={`flex items-center justify-between p-3 rounded-lg transition-colors ${
                      player.id === currentUser?.id
                        ? "bg-slate-700 border border-slate-600"
                        : "bg-slate-800 hover:bg-slate-700"
                    }`}
                  >
                    <div className="flex items-center space-x-4">
                      <div className="flex items-center justify-center w-8">
                        {player.rank <= 3 ? getRankIcon(player.rank) : player.rank}
                      </div>
                      <Avatar>
                        <AvatarImage src={player.avatar || "/placeholder.svg"} alt={player.username} />
                        <AvatarFallback>{player.username.substring(0, 2).toUpperCase()}</AvatarFallback>
                      </Avatar>
                      <div>
                        <p className="font-medium">{player.username}</p>
                        <p className="text-sm text-slate-400">Score: {player.score.toLocaleString()}</p>
                      </div>
                    </div>
                    <div className="flex items-center space-x-2">
                      <div className="text-sm text-slate-400 flex items-center">
                        {getRankChangeIcon(player.rank, player.previousRank)}
                        <span className="ml-1">
                          {player.previousRank && player.rank !== player.previousRank
                            ? Math.abs(player.rank - player.previousRank)
                            : ""}
                        </span>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="submit">
          <ScoreSubmissionForm
            onSubmit={(game, score) => {
              toast({
                title: "Score submitted",
                description: `Your score of ${score} for ${game} has been recorded`,
              })
            }}
          />
        </TabsContent>

        <TabsContent value="profile">
          {isLoggedIn && currentUser ? (
            <UserProfile user={currentUser} />
          ) : (
            <Card className="bg-slate-800 border-slate-700">
              <CardContent className="pt-6">
                <div className="text-center py-8">
                  <p className="mb-4">Please log in to view your profile</p>
                  <Button>Log In</Button>
                </div>
              </CardContent>
            </Card>
          )}
        </TabsContent>
      </Tabs>
    </div>
  )
}
