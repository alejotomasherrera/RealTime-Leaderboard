import { Skeleton } from "@/components/ui/skeleton"
import { Card, CardContent, CardHeader } from "@/components/ui/card"

export function LeaderboardSkeleton() {
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <Skeleton className="h-8 w-48 bg-slate-700" />
          <Skeleton className="h-4 w-32 mt-2 bg-slate-700" />
        </div>
        <div className="text-right">
          <Skeleton className="h-4 w-20 ml-auto bg-slate-700" />
          <Skeleton className="h-8 w-16 mt-2 ml-auto bg-slate-700" />
        </div>
      </div>

      <div className="w-full">
        <div className="grid w-full grid-cols-3 mb-6">
          <Skeleton className="h-10 rounded-l-md bg-slate-700" />
          <Skeleton className="h-10 bg-slate-700" />
          <Skeleton className="h-10 rounded-r-md bg-slate-700" />
        </div>

        <Card className="bg-slate-800 border-slate-700">
          <CardHeader className="pb-2">
            <Skeleton className="h-6 w-32 bg-slate-700" />
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {Array(10)
                .fill(0)
                .map((_, i) => (
                  <div key={i} className="flex items-center justify-between p-3 rounded-lg bg-slate-700">
                    <div className="flex items-center space-x-4">
                      <Skeleton className="h-8 w-8 rounded-full bg-slate-600" />
                      <div>
                        <Skeleton className="h-5 w-32 bg-slate-600" />
                        <Skeleton className="h-4 w-20 mt-2 bg-slate-600" />
                      </div>
                    </div>
                    <Skeleton className="h-6 w-16 bg-slate-600" />
                  </div>
                ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
