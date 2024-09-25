//
//  map_locationsApp.swift
//  map-locations
//
//  Created by Aaron Haser on 9/23/24.
//

import SwiftUI
import SwiftData

@main
struct map_locationsApp: App {
    var sharedModelContainer: ModelContainer = {
        let schema = Schema([
            Location.self, // Add Location to the schema
        ])
        let modelConfiguration = ModelConfiguration(schema: schema, isStoredInMemoryOnly: false)

        do {
            return try ModelContainer(for: schema, configurations: [modelConfiguration])
        } catch {
            fatalError("Could not create ModelContainer: \(error)")
        }
    }()

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
        .modelContainer(sharedModelContainer)
    }
}
