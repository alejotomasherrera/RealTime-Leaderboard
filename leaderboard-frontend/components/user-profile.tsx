"use client"

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Badge } from "@/components/ui/badge"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { ArrowUpRight, ArrowDownRight, Minus, Trophy, Medal, Clock } from "lucide-react"

interface User {
  id: string
  username: string
  score: number
  rank: number
  avatar?: string
  previousRank?: number
}

interface UserProfileProps {
  user: User
}

export default function UserProfile({ user }: UserProfileProps) {
  // Sample data for user history and achievements
  const scoreHistory = [
    { game: "Arcade Adventure", score: 350, date: "2023-04-15", change: "+50" },
    { game: "Space Shooter", score: 220, date: "2023-04-12", change: "+20" },
    { game: "Puzzle Master", score: 180, date: "2023-04-10", change: "-10" },
    { game: "Racing Legends", score: 100, date: "2023-04-05", change: "+100" },
  ]

  const achievements = [
    {
      name: "First Victory",
      description: "Win your first game",
      date: "2023-03-01",
      icon: <Trophy className="h-5 w-5" />,
    },
    {
      name: "Top 10",
      description: "Reach the top 10 on the leaderboard",
      date: "2023-03-15",
      icon: <Medal className="h-5 w-5" />,
    },
    {
      name: "Dedicated Player",
      description: "Play for 10 consecutive days",
      date: "2023-04-01",
      icon: <Clock className="h-5 w-5" />,
    },
  ]

  const getRankChangeIcon = () => {
    if (!user.previousRank || user.rank === user.previousRank) {
      return <Minus className="h-4 w-4 text-gray-400" />
    } else if (user.rank < user.previousRank) {
      return <ArrowUpRight className="h-4 w-4 text-green-500" />
    } else {
      return <ArrowDownRight className="h-4 w-4 text-red-500" />
    }
  }

  return (
    <div className="space-y-6">
      <Card className="bg-slate-800 border-slate-700">
        <CardHeader className="pb-2">
          <div className="flex items-start justify-between">
            <div className="flex items-center space-x-4">
              <Avatar className="h-16 w-16">
                <AvatarImage src={user.avatar || "/placeholder.svg"} alt={user.username} />
                <AvatarFallback className="text-lg">{user.username.substring(0, 2).toUpperCase()}</AvatarFallback>
              </Avatar>
              <div>
                <CardTitle className="text-2xl">{user.username}</CardTitle>
                <CardDescription>Member since March 2023</CardDescription>
                <div className="flex items-center mt-1 space-x-2">
                  <Badge variant="outline" className="bg-slate-700">
                    Rank #{user.rank}
                  </Badge>
                  <Badge variant="outline" className="bg-slate-700">
                    Score: {user.score}
                  </Badge>
                </div>
              </div>
            </div>
            <div className="flex items-center space-x-1 text-sm">
              {getRankChangeIcon()}
              <span>
                {user.previousRank && user.rank !== user.previousRank
                  ? `${user.rank < user.previousRank ? "+" : "-"}${Math.abs(user.rank - user.previousRank)} ranks`
                  : "No change"}
              </span>
            </div>
          </div>
        </CardHeader>
      </Card>

      <Tabs defaultValue="history">
        <TabsList className="grid w-full grid-cols-2">
          <TabsTrigger value="history">Score History</TabsTrigger>
          <TabsTrigger value="achievements">Achievements</TabsTrigger>
        </TabsList>

        <TabsContent value="history" className="mt-4">
          <Card className="bg-slate-800 border-slate-700">
            <CardHeader className="pb-2">
              <CardTitle>Recent Scores</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {scoreHistory.map((entry, index) => (
                  <div key={index} className="flex items-center justify-between p-3 rounded-lg bg-slate-700">
                    <div>
                      <p className="font-medium">{entry.game}</p>
                      <p className="text-sm text-slate-300">{entry.date}</p>
                    </div>
                    <div className="text-right">
                      <p className="font-bold">{entry.score}</p>
                      <p className={`text-sm ${entry.change.startsWith("+") ? "text-green-400" : "text-red-400"}`}>
                        {entry.change}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="achievements" className="mt-4">
          <Card className="bg-slate-800 border-slate-700">
            <CardHeader className="pb-2">
              <CardTitle>Your Achievements</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {achievements.map((achievement, index) => (
                  <div key={index} className="flex items-center space-x-4 p-3 rounded-lg bg-slate-700">
                    <div className="flex-shrink-0 h-10 w-10 rounded-full bg-slate-600 flex items-center justify-center text-yellow-400">
                      {achievement.icon}
                    </div>
                    <div>
                      <p className="font-medium">{achievement.name}</p>
                      <p className="text-sm text-slate-300">{achievement.description}</p>
                      <p className="text-xs text-slate-400 mt-1">Unlocked: {achievement.date}</p>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
