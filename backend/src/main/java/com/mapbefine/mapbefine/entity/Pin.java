package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.*;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Pin extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 50)
	private String name;
	@Lob
	@Column(nullable = false, length = 1000)
	private String description;
	@ManyToOne
	@JoinColumn(name = "location_id", nullable = false)
	private Location location;
	@Column(nullable = false)
	@ColumnDefault(value = "false")
	private boolean isDeleted = false;
	@ManyToOne
	@JoinColumn(name = "topic_id", nullable = false)
	private Topic topic;

	public Pin(
		String name,
		String description,
		Location location,
		Topic topic
	) {
		this.name = name;
		this.description = description;
		this.location = location;
		this.topic = topic;
	}

	public Pin duplicate(Topic topic) {
		return new Pin(
			name,
			description,
			location,
			topic
		);
	}

}
