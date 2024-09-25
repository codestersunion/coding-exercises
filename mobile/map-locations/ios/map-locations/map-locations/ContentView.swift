//
//  ContentView.swift
//  map-locations
//
//  Created by Aaron Haser on 9/23/24.
//

import SwiftUI
import SwiftData
import MapKit

let dataUrl = "https://raw.githubusercontent.com/codestersunion/coding-exercises/master/mobile/map-locations/locations.json"

struct ContentView: View {
    @State private var locations: [Location] = [];
    @State private var showAlert = false;
    @State private var selectedLocation: Location? = nil;
    @State private var selectedType: String? = nil;
    @State private var locationTypes: Array<String> = [];
    
    var filteredLocations: [Location] {
        locations.filter({ location in
            selectedType == nil || location.locationType == selectedType
        })
    }

    var body: some View {
        ZStack(alignment: .bottom) {
            Map {
                ForEach(filteredLocations) { location in
                    Annotation("", coordinate: location.coordinates()) {
                        MarkerView(location: location).onTapGesture {
                            withAnimation {
                                selectedLocation = location
                            }
                        }
                    }
                }
            }
            .onAppear() {
                fetchLocationData(from: dataUrl) { locations in
                    if let locations = locations {
                        self.locations = locations
                        self.locationTypes = Array(Set(locations.map { $0.locationType })).sorted()
                    } else {
                        showAlert = true
                    }
                }
                
            }
            VStack {
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 10) {
                        ForEach(locationTypes, id: \.self) { type in
                            RoundedRectangle(cornerRadius: 10)
                                .fill(selectedType == type ? Color.blue : Color.white)
                                .frame(width: 100, height: 50)
                                .overlay(
                                    RoundedRectangle(cornerRadius: 10)
                                        .stroke(.black, lineWidth: 2)
                                )
                                .overlay(
                                    Text("\(type.capitalized)")
                                        .foregroundColor(selectedType == type ? Color.white : Color.black)
                                ).onTapGesture {
                                    withAnimation {
                                        // unselect the type on second tap
                                        selectedType = (selectedType == nil || selectedType != type) ? type : nil
                                        // unselect the location if it isn't the correct type
                                        if(selectedLocation?.locationType != selectedType) {
                                            selectedLocation = nil
                                        }
                                    }
                                }
                        }
                    }
                    .padding()
                }
                Spacer()
                // show the detail if the selected location is in the filtered location set
                if(selectedLocation != nil && (selectedLocation?.locationType == selectedType || selectedType == nil)) {
                    let location: Location! = selectedLocation!
                    VStack(alignment: .leading) {
                        Text("\(location.name)").font(.headline)
                        Text("\(location.locationType)".capitalized)
                        Text("\(location.locationDescription)")
                        Text("Estimated revenue (millions): \(String(format: "%g", location.estimatedRevenueMillions))")
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding()
                    .background(Color.white.opacity(0.9))
                    .cornerRadius(10)
                    .overlay(
                        RoundedRectangle(cornerRadius: 20)
                            .stroke(.black, lineWidth: 2)
                    )
                    .padding(.horizontal, 16)
                }
            }
            
        }
    }

    private func fetchLocationData(from urlString: String, completion: @escaping ([Location]?) -> Void) {
        guard let url = URL(string: urlString) else {
            print("Invalid URL")
            completion(nil)
            return
        }
        
        let task = URLSession.shared.dataTask(with: url) { data, response, error in
            if let error = error {
                print("Error fetching data: \(error)")
                completion(nil)
                return
            }
            
            guard let data = data else {
                print("No data received")
                completion(nil)
                return
            }
            
            let locations = parseLocationData(from: data)
            completion(locations)
        }
        
        task.resume()
    }
}

struct MarkerView: View {
    public var location: Location
    
    var body: some View {
        Image(systemName: "mappin.circle.fill")
            .font(.title)
            .foregroundColor(.red)
    }
}

#Preview {
    ContentView()
        .modelContainer(for: Location.self, inMemory: true)
}
