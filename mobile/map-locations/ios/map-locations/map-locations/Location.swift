import Foundation
import SwiftData
import CoreLocation


struct Attribute: Codable {
    var type: String
    var value: String
    
    init(type: String, value: String) {
        self.type = type
        self.value = value
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.type = try container.decode(String.self, forKey: .type)
        if let value = try? container.decode(Double.self, forKey: .value) {
            self.value = value.description
        } else {
            self.value = try container.decode(String.self, forKey: .value)
        }
    }
    enum CodingKeys: String, CodingKey {
        case type
        case value
    }
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(type, forKey: .type)
        try container.encode(value, forKey: .value)
    }
    
}

@Model
final class Location: Codable, Identifiable {
    @Attribute(.unique) var id: String
    var latitude: Double
    var longitude: Double
    var name: String
    var locationType: String
    var estimatedRevenueMillions: Double
    var locationDescription: String
    
    init(id: Int, latitude: Double, longitude: Double, attributes: [Attribute]) {
        self.id = id.description
        self.latitude = latitude
        self.longitude = longitude
        self.name = ""
        self.locationType = ""
        self.estimatedRevenueMillions = -0
        self.locationDescription = ""
        self.updateFromAttributes(attributes)
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        id = try container.decode(Double.self, forKey: .id).description
        latitude = try container.decode(Double.self, forKey: .latitude)
        longitude = try container.decode(Double.self, forKey: .longitude)
        name = ""
        locationType = ""
        estimatedRevenueMillions = -0
        locationDescription = ""
        let attributes = try container.decode([Attribute].self, forKey: .attributes)
        updateFromAttributes(attributes)
    }
    
    private func updateFromAttributes(_ attributes: [Attribute]) {
        for attribute in attributes {
            switch attribute.type {
            case "location_type":
                locationType = attribute.value
            case "estimated_revenue_millions":
                estimatedRevenueMillions = Double(attribute.value) ?? -0
            case "name":
                name = attribute.value
            case "description":
                locationDescription = attribute.value
            default:
                break
            }
        }
    }
    
    enum CodingKeys: String, CodingKey {
        case id, latitude, longitude, attributes
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(id, forKey: .id)
        try container.encode(latitude, forKey: .latitude)
        try container.encode(longitude, forKey: .longitude)
        
        var attributes: [Attribute] = []
        attributes.append(Attribute(type: "location_type", value: locationType))
        attributes.append(Attribute(type: "name", value: name))
        attributes.append(Attribute(type: "description", value: locationDescription))
        attributes.append(Attribute(type: "estimated_revenue_millions", value: String(estimatedRevenueMillions)))
        
        try container.encode(attributes, forKey: .attributes)
    }
    
    func coordinates() -> CLLocationCoordinate2D {
        return CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
    }
}

func parseLocationData(from jsonData: Data) -> [Location]? {
    let decoder = JSONDecoder()
    do {
        let locations = try decoder.decode([Location].self, from: jsonData)
        return locations
    } catch {
        print("Error decoding JSON data: \(error)")
        return nil
    }
}
