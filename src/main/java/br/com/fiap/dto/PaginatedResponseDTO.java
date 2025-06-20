package br.com.fiap.dto;

import java.util.List;

public class PaginatedResponseDTO<T> {
	private List<T> content;
	private long totalElements;
	private int pageNumber;
	private int pageSize;

	public PaginatedResponseDTO(List<T> content, long totalElements, int pageNumber, int pageSize) {
		this.content = content;
		this.totalElements = totalElements;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}

	public List<T> getContent() {
		return content;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}
}
